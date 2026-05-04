package com.co.inventoryconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSalesProductConfig {

    // ================= EXCHANGES =================

    @Value("${rabbitmq.exchange.sales.create.request}")
    private String salesCreateRequestExchange;

    @Value("${rabbitmq.exchange.sales.response}")
    private String salesResponseExchange;

    // ================= QUEUES =================

    @Value("${rabbitmq.queue.sales.create.request}")
    private String salesCreateRequestQueue;

    @Value("${rabbitmq.queue.sales.response}")
    private String salesResponseQueue;

    // ================= ROUTING KEYS =================

    @Value("${rabbitmq.routing-key.sales.create.request}")
    private String salesCreateRequestRoutingKey;

    @Value("${rabbitmq.routing-key.sales.response}")
    private String salesResponseRoutingKey;

    // =========================================================
    // EXCHANGES
    // =========================================================

    @Bean
    public DirectExchange salesCreateRequestExchange() {
        return new DirectExchange(salesCreateRequestExchange, true, false);
    }

    @Bean
    public DirectExchange salesResponseExchange() {
        return new DirectExchange(salesResponseExchange, true, false);
    }

    // =========================================================
    // QUEUES
    // =========================================================

    @Bean
    public Queue salesCreateRequestQueue() {
        return new Queue(salesCreateRequestQueue, true);
    }

    @Bean
    public Queue salesResponseQueue() {
        return new Queue(salesResponseQueue, true);
    }

    // =========================================================
    // BINDINGS
    // =========================================================

    @Bean
    public Binding salesCreateRequestBinding() {
        return BindingBuilder
                .bind(salesCreateRequestQueue())
                .to(salesCreateRequestExchange())
                .with(salesCreateRequestRoutingKey);
    }

    @Bean
    public Binding salesResponseBinding() {
        return BindingBuilder
                .bind(salesResponseQueue())
                .to(salesResponseExchange())
                .with(salesResponseRoutingKey);
    }
}