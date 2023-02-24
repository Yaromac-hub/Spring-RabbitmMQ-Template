package com.yaromac.springamqptemplate.amqp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MessageListenerManager {

	public static final Logger log = LogManager.getLogger("LoggerForDebugging");
	
	@Autowired
	RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

	public void stopListener(final String listenerId) {
		if (this.rabbitListenerEndpointRegistry.getListenerContainer(listenerId).isRunning()) {
			log.info("stoping listener");
			this.rabbitListenerEndpointRegistry.getListenerContainer(listenerId).stop();
		}
	}

	public void startListener(final String listenerId) {
		if (!this.rabbitListenerEndpointRegistry.getListenerContainer(listenerId).isRunning()) {
			log.info("starting listener");
			this.rabbitListenerEndpointRegistry.getListenerContainer(listenerId).start();
		}
	}

	public Set<String> getListeners() {
		return this.rabbitListenerEndpointRegistry.getListenerContainerIds();
	}

	public boolean isRunning(final String listenerId) {
		return this.rabbitListenerEndpointRegistry.getListenerContainer(listenerId).isRunning();
	}

}
