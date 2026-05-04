package com.co.inventoryconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProductConfig {

    @Value("${rabbitmq.exchange.product}")
    private String productExchange;

    @Value("${rabbitmq.queue.product.create}")
    private String productCreateQueue;

    @Value("${rabbitmq.queue.product.update}")
    private String productUpdateQueue;

    @Value("${rabbitmq.queue.product.patch}")
    private String productPatchQueue;

    @Value("${rabbitmq.queue.product.delete}")
    private String productDeleteQueue;

    @Value("${rabbitmq.routing-key.product.create}")
    private String productCreateRoutingKey;

    @Value("${rabbitmq.routing-key.product.update}")
    private String productUpdateRoutingKey;

    @Value("${rabbitmq.routing-key.product.patch}")
    private String productPatchRoutingKey;

    @Value("${rabbitmq.routing-key.product.delete}")
    private String productDeleteRoutingKey;

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(productExchange, true, false);
    }

    @Bean
    public Queue productCreateQueue() {
        return new Queue(productCreateQueue, true);
    }

    @Bean
    public Queue productUpdateQueue() {
        return new Queue(productUpdateQueue, true);
    }

    @Bean
    public Queue productPatchQueue() {
        return new Queue(productPatchQueue, true);
    }

    @Bean
    public Queue productDeleteQueue() {
        return new Queue(productDeleteQueue, true);
    }

    @Bean
    public Binding productCreateBinding() {
        return BindingBuilder
                .bind(productCreateQueue())
                .to(productExchange())
                .with(productCreateRoutingKey);
    }

    @Bean
    public Binding productUpdateBinding() {
        return BindingBuilder
                .bind(productUpdateQueue())
                .to(productExchange())
                .with(productUpdateRoutingKey);
    }

    @Bean
    public Binding productPatchBinding() {
        return BindingBuilder
                .bind(productPatchQueue())
                .to(productExchange())
                .with(productPatchRoutingKey);
    }

    @Bean
    public Binding productDeleteBinding() {
        return BindingBuilder
                .bind(productDeleteQueue())
                .to(productExchange())
                .with(productDeleteRoutingKey);
    }
}