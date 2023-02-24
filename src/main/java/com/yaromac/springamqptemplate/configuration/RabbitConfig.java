package com.yaromac.springamqptemplate.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yaromac.springamqptemplate.amqp.CustomErrorHandler;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableRabbit
public class RabbitConfig {
	
	public static String GUESS_COLOR_QUEUE = "guess.color.queue";
	public static String GUESS_NUMBER_QUEUE = "guess.number.queue";
	public static String GUESS_COLOR_WAIT_QUEUE = "guess.color.error.queue";
	public static String GUESS_NUMBER_WAIT_QUEUE = "guess.number.error.queue";
	public static String QUIZ_HEADER_EXCHANGE = "quiz-header-exchange";

	private static int tokenConcurrentConsumers = 1;
	private static int tokenMaxConcurrentConsumers = 2;
	private static int workConcurrentConsumers = 1;
	private static int workMaxConcurrentConsumers = 2;
	private static int prefetchCount = 1;
	private static Long shutdownTimeout = 300000l;

	@Bean
	public MessageConverter messageConverter(ObjectMapper objectMapper) {
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(objectMapper);
		jsonConverter.setAlwaysConvertToInferredType(true);
		return jsonConverter;
	}

	@Bean
	public ErrorHandler errorHandler() {
		return new CustomErrorHandler();
	}

	@Bean("containerFactoryColor")
	public SimpleRabbitListenerContainerFactory containerFactoryWork(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
	  SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	  factory.setConcurrentConsumers(workConcurrentConsumers);
	  factory.setMaxConcurrentConsumers(workMaxConcurrentConsumers);
	  factory.setContainerCustomizer(c->c.setShutdownTimeout(shutdownTimeout));
	  factory.setContainerCustomizer(c->c.setQueueNames(GUESS_COLOR_QUEUE));
	  factory.setPrefetchCount(prefetchCount);
	  factory.setErrorHandler(errorHandler());
	  configurer.configure(factory, connectionFactory);
	  return factory;
	}

	@Bean("containerFactoryNumber")
	public SimpleRabbitListenerContainerFactory containerFactoryToken(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
	  SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	  factory.setConcurrentConsumers(tokenConcurrentConsumers);
	  factory.setMaxConcurrentConsumers(tokenMaxConcurrentConsumers);
	  factory.setContainerCustomizer(c->c.setShutdownTimeout(shutdownTimeout));
	  factory.setContainerCustomizer(c->c.setQueueNames(GUESS_NUMBER_QUEUE));
	  factory.setPrefetchCount(prefetchCount);
	  factory.setErrorHandler(errorHandler());
	  configurer.configure(factory, connectionFactory);
	  return factory;
	}

	@Bean
	public Channel queueDeclaration(ConnectionFactory connectionFactory) {

		Channel channel = connectionFactory.createConnection().createChannel(true);

		Map<String, Object> queueColorWaitArgs = new HashMap<>();
		queueColorWaitArgs.put("x-dead-letter-routing-key", GUESS_COLOR_QUEUE);
		queueColorWaitArgs.put("x-message-ttl", 5000);
		queueColorWaitArgs.put("x-dead-letter-exchange", "");

		Map<String, Object> queueNumberWaitArgs = new HashMap<>();
		queueNumberWaitArgs.put("x-dead-letter-routing-key", GUESS_NUMBER_QUEUE);
		queueNumberWaitArgs.put("x-message-ttl", 5000);
		queueNumberWaitArgs.put("x-dead-letter-exchange", "");

		Map<String, Object> queueColorArgs = new HashMap<>();
		queueColorArgs.put("x-dead-letter-exchange", "");
		queueColorArgs.put("x-dead-letter-routing-key", GUESS_COLOR_WAIT_QUEUE);

		Map<String, Object> queueNumberArgs = new HashMap<>();
		queueNumberArgs.put("x-dead-letter-exchange", "");
		queueNumberArgs.put("x-dead-letter-routing-key", GUESS_NUMBER_WAIT_QUEUE);


		Map<String, Object> exchangeColorBinds = new HashMap<>();
		exchangeColorBinds.put("quiz_header","guess_color");

		Map<String, Object> exchangeNumberBinds = new HashMap<>();
		exchangeNumberBinds.put("quiz_header","guess_number");


		try {
			channel.queueDeclare(GUESS_COLOR_QUEUE, true, false, false, queueColorArgs);
			channel.queueDeclare(GUESS_NUMBER_QUEUE, true, false, false, queueNumberArgs);

			channel.queueDeclare(GUESS_COLOR_WAIT_QUEUE, true, false, false, queueColorWaitArgs);
			channel.queueDeclare(GUESS_NUMBER_WAIT_QUEUE, true, false, false, queueNumberWaitArgs);

			channel.exchangeDeclare(QUIZ_HEADER_EXCHANGE,  BuiltinExchangeType.HEADERS, true);
			channel.queueBind(GUESS_COLOR_QUEUE, QUIZ_HEADER_EXCHANGE, "", exchangeColorBinds);
			channel.queueBind(GUESS_NUMBER_QUEUE, QUIZ_HEADER_EXCHANGE, "", exchangeNumberBinds);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return  channel;
	}

}
