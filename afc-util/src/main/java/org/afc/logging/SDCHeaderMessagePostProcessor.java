package org.afc.logging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

public class SDCHeaderMessagePostProcessor implements MessagePostProcessor {

	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		message.getMessageProperties().setHeader(SDC.SDC, SDC.auto());
		return message;
	}
}