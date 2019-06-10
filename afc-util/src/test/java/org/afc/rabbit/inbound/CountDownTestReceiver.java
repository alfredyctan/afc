package org.afc.rabbit.inbound;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import lombok.Getter;

@Getter
public class CountDownTestReceiver<T> {

	private static final Logger logger = LoggerFactory.getLogger(CountDownTestReceiver.class);

	protected List<T> messages;

	protected CountDownLatch latch;
	
	protected String id;
	
	protected RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;   

	public CountDownTestReceiver(String id, RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry) {
		this.id = id;
		this.rabbitListenerEndpointRegistry =  rabbitListenerEndpointRegistry;
		this.messages = new LinkedList<>();
	}
	
	
	public void doReceive(T t, Message message) throws InterruptedException {
		logger.info("message received : {}", t);
		messages.add(t);
		latch.countDown();
	}

	public CountDownLatch countDown(int count) {
		latch = new CountDownLatch(count);
		return latch;
	}

	public void reset() {
		messages.clear();
		latch = null;
	}

	public void stop() {
		rabbitListenerEndpointRegistry.getListenerContainers().forEach(listener -> {
			if (((SimpleMessageListenerContainer)listener).getListenerId().equals(id)) {
				System.out.println("stopping rabbit listener " + listener);
				listener.stop();
			}
		});
	}	
}
