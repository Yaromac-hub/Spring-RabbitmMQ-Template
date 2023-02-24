package com.yaromac.springamqptemplate.amqp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageListener {

	@Autowired
	MessageProducerService produceMessageService;

	public static final Logger log = LogManager.getLogger("LoggerForDebugging");

	@RabbitListener(containerFactory = "containerFactoryColor", id = "mtlistenerColor", autoStartup = "false")
	public void listenerTokenQueue(String tokenMessage) {

	}

	@RabbitListener(containerFactory = "containerFactoryNumber", id = "mtlistenerNumber", autoStartup = "false")
	public void listenerWorkQueue(String requestMessage) {

	}

}
