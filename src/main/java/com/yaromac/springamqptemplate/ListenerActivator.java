package com.yaromac.springamqptemplate;

import com.yaromac.springamqptemplate.amqp.MessageListenerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

@Component
@EnableScheduling
public class ListenerActivator implements CommandLineRunner {

	public static final Logger log = LogManager.getLogger("LoggerForDebugging");

	private static final String TOKEN_LISTENER_ID = "mtlistenerColor";
	private static final String WORK_LISTENER_ID = "mtlistenerNumber";

	@Autowired
	MessageListenerManager messageListenerManager;

	public void run(String... args) throws InterruptedException, JAXBException {

		messageListenerManager.startListener(TOKEN_LISTENER_ID);
		messageListenerManager.startListener(WORK_LISTENER_ID);

		Thread.sleep(60000l);

		messageListenerManager.stopListener(TOKEN_LISTENER_ID);
		messageListenerManager.stopListener(WORK_LISTENER_ID);

	}

}