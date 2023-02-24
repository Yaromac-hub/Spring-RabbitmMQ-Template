package com.yaromac.springamqptemplate.amqp;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

public class CustomErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        if (!(isInstanceOfRetryableCause(t.getCause()))) {
            throw new AmqpRejectAndDontRequeueException("Error Handler converted exception to fatal", t);
        }
    }

    private boolean isInstanceOfRetryableCause(Throwable cause) {
        return (true);
    }
}
