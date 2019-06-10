package org.afc.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.amqp.core.Message;

@Aspect
public class SDCRabbitAspect {

	@Before("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
	public void onMessage(JoinPoint joinPoint) {
		doSetSDC(joinPoint.getArgs());
	}

	@Before("execution(public * org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter.onMessage(..))")
	public void export(JoinPoint joinPoint) {
		doSetSDC(joinPoint.getArgs());
	}

	private void doSetSDC(Object[] args) {
		if (args == null) {
			return;
		}
		for (Object arg : args) {
			if (arg instanceof Message) {
				SDC.set((String) ((Message) arg).getMessageProperties().getHeaders().get(SDC.SDC));
				return;
			}
		}
	}
}
