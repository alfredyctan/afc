package org.afc.logging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SDCRabbitContext {

	private static final Logger logger = LoggerFactory.getLogger(SDCRabbitContext.class);

	@Autowired
	private List<RabbitTemplate> rabbitTemplates;

	@Bean
	public SDCHeaderMessagePostProcessor beforePublishPostProcessor() {
		SDCHeaderMessagePostProcessor processor = new SDCHeaderMessagePostProcessor();
		for (RabbitTemplate rabbitTemplate : rabbitTemplates) {
			rabbitTemplate.setBeforePublishPostProcessors(processor);
		}
		return processor;
	}

	@Bean
	public SDCRabbitAspect sdcRabbitTemplateAspect() {
		return new SDCRabbitAspect();
	}
}