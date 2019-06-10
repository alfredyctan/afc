package org.afc.rabbit.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.afc.outbound.OutboundPublisher;
import org.afc.util.AutoString;

public class RabbitOutboundPublisher<T> implements OutboundPublisher<T> {

	private final Logger logger;
	
	private String name;
	
	private RabbitTemplate rabbitTemplate;

	private String exchange;

	private String routingKey;

	public RabbitOutboundPublisher(String name, RabbitTemplate rabbitTemplate, String exchange, String routingKey) {
		this.rabbitTemplate = rabbitTemplate;
		this.name = name;
		this.exchange = exchange;
		this.routingKey = routingKey;
		this.logger = LoggerFactory.getLogger(RabbitOutboundPublisher.class.getName() + '.' + name);
		logger.info("exchange for {} : {}", name, exchange);
		logger.info("routing key for {} : {}", name, routingKey);
	}

	@Override
	public void publish(T message) {
		logger.info("{} to be published : {}", name, new AutoString(message));
		rabbitTemplate.convertAndSend(exchange, routingKey, message);
	}
}
