package com.co.inventoryconsumer.config.rabbit;

import org.springframework.amqp.core.*;

public abstract class AbstractRabbitConfig {

    protected DirectExchange buildExchange(String name) {
        return new DirectExchange(name, true, false);
    }

    protected Queue buildQueue(String name) {
        return new Queue(name, true);
    }

    protected Binding buildBinding(Queue queue,
                                   DirectExchange exchange,
                                   String routingKey) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
    }
}