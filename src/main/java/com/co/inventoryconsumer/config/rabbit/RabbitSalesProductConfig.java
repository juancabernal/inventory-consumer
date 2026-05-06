package com.co.inventoryconsumer.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSalesProductConfig extends AbstractRabbitConfig {

    @Value("${rabbitmq.exchange.sales.create.request}")
    private String requestExchangeName;

    @Value("${rabbitmq.exchange.sales.create.response}")
    private String responseExchangeName;

    @Value("${rabbitmq.queue.sales.create.request}")
    private String requestQueue;

    @Value("${rabbitmq.queue.sales.create.response}")
    private String responseQueue;

    @Value("${rabbitmq.routing-key.sales.create.request}")
    private String requestKey;

    @Value("${rabbitmq.routing-key.sales.create.response}")
    private String responseKey;

    @Bean
    public DirectExchange salesCreateRequestExchange() {
        return buildExchange(requestExchangeName);
    }

    @Bean
    public DirectExchange salesResponseExchange() {
        return buildExchange(responseExchangeName);
    }

    @Bean
    public Queue salesCreateRequestQueue() {
        return buildQueue(requestQueue);
    }

    @Bean
    public Queue salesResponseQueue() {
        return buildQueue(responseQueue);
    }

    @Bean
    public Binding salesCreateRequestBinding() {
        return buildBinding(
                salesCreateRequestQueue(),
                salesCreateRequestExchange(),
                requestKey
        );
    }

    @Bean
    public Binding salesResponseBinding() {
        return buildBinding(
                salesResponseQueue(),
                salesResponseExchange(),
                responseKey
        );
    }
}