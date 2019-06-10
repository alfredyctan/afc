package org.afc.rabbit.inbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import org.afc.service.Service;
import org.afc.util.AutoString;

public class RabbitInboundReceiver<T> {

	private static final Logger logger = LoggerFactory.getLogger(RabbitInboundReceiver.class);

	protected Service<T, Void> service;  

	public RabbitInboundReceiver(Service<T, Void> service) {
		this.service = service;
	}
	
    protected void receive(T request, Message message) throws InterruptedException {
		try {
			logger.info("message received : {}", new AutoString(request));
			service.serve(request);
		} catch (Exception e) {
			logger.error("failed to process : " + message, e);
		}
	}
}
