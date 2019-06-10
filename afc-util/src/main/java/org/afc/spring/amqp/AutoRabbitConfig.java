package org.afc.spring.amqp;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoRabbitConfig {

	@Bean
	public RabbitTemplate rabbitTemplate(
			CachingConnectionFactory factory,
			AutoBindRecoveryListener recoveryListener,
			@Qualifier("jackson2JsonMessageConverter") MessageConverter messageConverter) {
		factory.addConnectionListener(recoveryListener);
		factory.addChannelListener(recoveryListener);
		RabbitTemplate template = new RabbitTemplate(factory);
		template.setMessageConverter(messageConverter);
		return template;
	}
	
}
