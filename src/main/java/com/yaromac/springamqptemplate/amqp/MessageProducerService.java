package com.yaromac.springamqptemplate.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class MessageProducerService {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public String produceMessage(Object message, String routingKey) {
    	rabbitTemplate.convertAndSend(routingKey, message);
        return "Message(" + message + ")" + " has been produced.";
    }
    
    public String produceTokenMessage(Object message, String routingKey) {
    	rabbitTemplate.convertAndSend(routingKey, message);
        return "Message(" + message + ")" + " has been produced.";
    }
    
    public String produceRequestMessage(Object message, String routingKey) {
    	rabbitTemplate.convertAndSend(routingKey, message);
        return "Message(" + message + ")" + " has been produced.";
    }
}