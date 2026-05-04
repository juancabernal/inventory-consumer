package com.co.inventoryconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTransferConfig {

    @Value("${rabbitmq.exchange.transfer.request:create.transfer.request.exchange}")
    private String transferRequestExchange;

    @Value("${rabbitmq.exchange.transfer.response:create.transfer.response.exchange}")
    private String transferResponseExchange;

    @Value("${rabbitmq.exchange.transfer.status.request:update.transfer.status.request.exchange}")
    private String transferStatusRequestExchange;

    @Value("${rabbitmq.queue.transfer.request:create.transfer.request.queue}")
    private String transferRequestQueue;

    @Value("${rabbitmq.queue.transfer.response:create.transfer.response.queue}")
    private String transferResponseQueue;

    @Value("${rabbitmq.queue.transfer.status.request:update.transfer.status.request.queue}")
    private String transferStatusRequestQueue;

    @Value("${rabbitmq.routing-key.transfer.request:create.transfer.request}")
    private String transferRequestRoutingKey;

    @Value("${rabbitmq.routing-key.transfer.response:create.transfer.response}")
    private String transferResponseRoutingKey;

    @Value("${rabbitmq.routing-key.transfer.status.request:update.transfer.status.request}")
    private String transferStatusRequestRoutingKey;

    @Bean
    public DirectExchange transferRequestExchange() {
        return new DirectExchange(transferRequestExchange, true, false);
    }

    @Bean
    public DirectExchange transferResponseExchange() {
        return new DirectExchange(transferResponseExchange, true, false);
    }

    @Bean
    public DirectExchange transferStatusRequestExchange() {
        return new DirectExchange(transferStatusRequestExchange, true, false);
    }

    @Bean
    public Queue transferRequestQueue() {
        return new Queue(transferRequestQueue, true);
    }

    @Bean
    public Queue transferResponseQueue() {
        return new Queue(transferResponseQueue, true);
    }

    @Bean
    public Queue transferStatusRequestQueue() {
        return new Queue(transferStatusRequestQueue, true);
    }

    @Bean
    public Binding transferRequestBinding() {
        return BindingBuilder
                .bind(transferRequestQueue())
                .to(transferRequestExchange())
                .with(transferRequestRoutingKey);
    }

    @Bean
    public Binding transferResponseBinding() {
        return BindingBuilder
                .bind(transferResponseQueue())
                .to(transferResponseExchange())
                .with(transferResponseRoutingKey);
    }

    @Bean
    public Binding transferStatusRequestBinding() {
        return BindingBuilder
                .bind(transferStatusRequestQueue())
                .to(transferStatusRequestExchange())
                .with(transferStatusRequestRoutingKey);
    }
}
