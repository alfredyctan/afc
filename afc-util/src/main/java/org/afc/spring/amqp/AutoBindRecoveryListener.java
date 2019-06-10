package org.afc.spring.amqp;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.connection.ChannelListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;

public class AutoBindRecoveryListener implements ConnectionListener, ChannelListener {

	private static final Logger logger = LoggerFactory.getLogger(AutoBindRecoveryListener.class);
	
	private AtomicBoolean down;
	
	private AmqpAdmin amqpAdmin;
	
	private Map<Object, List<Method>> bindings;
	
	public AutoBindRecoveryListener(AmqpAdmin amqpAdmin, Object... bindingConfigs) {
		this.down = new AtomicBoolean(false);
		this.amqpAdmin = amqpAdmin;
		this.bindings = new HashMap<>();

		for (Object bindingConfig : bindingConfigs) {
			List<Method> bindingMethods = new LinkedList<>();
			for (Method method : bindingConfig.getClass().getDeclaredMethods()) {
				if (method.getReturnType().equals(Binding.class) && Arrays.equals(method.getParameterTypes(), new Class[] { AmqpAdmin.class })) {
					bindingMethods.add(method);
					logger.info("enable auto-rebind:[{}.{}]", bindingConfig.getClass().getSimpleName(), method.getName());
				}
			}
			bindings.put(bindingConfig, bindingMethods);
		}
	}

	@Override
	public void onCreate(Connection connection) {
		logger.info("on AMQP connection create:[{}]", connection); 
	}
	@Override
	public void onClose(Connection connection) {
		logger.info("on AMQP connection close:[{}]", connection); 
		synchronized(down) {
			down.set(true);
		}
	}
	
	@Override
	public void onShutDown(ShutdownSignalException signal) {
		logger.info("on AMQP connection/channel shutdown:[{}]", signal.toString()); 
		synchronized(down) {
			down.set(true);
		}
	}

	@Override
	public void onCreate(Channel channel, boolean transactional) {
		logger.info("on AMQP channel create:[{}], [{}]", channel, transactional);
		synchronized(down) {
			if (down.get()) {
				down.set(false);
				bindings.entrySet().stream().forEach(entry -> {
					entry.getValue().stream().forEach(bindingMethod -> {
						try {
							bindingMethod.invoke(entry.getKey(), amqpAdmin);
							logger.info("rebinded:[{}.{}]", entry.getKey().getClass().getSimpleName(), bindingMethod.getName());
						} catch (Throwable t) {
							logger.error("error on AMQP configuration rebind", t);
						}
					});
				});
			}
		}
	}
}
