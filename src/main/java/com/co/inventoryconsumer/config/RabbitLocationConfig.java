package com.co.inventoryconsumer.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitLocationConfig {

    @Value("${rabbitmq.exchange.location}")
    private String locationExchange;

    @Value("${rabbitmq.queue.location.create}")
    private String locationCreateQueue;

    @Value("${rabbitmq.queue.location.update}")
    private String locationUpdateQueue;

    @Value("${rabbitmq.queue.location.patch}")
    private String locationPatchQueue;



    @Value("${rabbitmq.routing-key.location.create}")
    private String locationCreateRoutingKey;

    @Value("${rabbitmq.routing-key.location.update}")
    private String locationUpdateRoutingKey;

    @Value("${rabbitmq.routing-key.location.patch}")
    private String locationPatchRoutingKey;



    @Bean
    public DirectExchange locationExchange() {
        return new DirectExchange(locationExchange, true, false);
    }

    @Bean
    public Queue locationCreateQueue() {
        return new Queue(locationCreateQueue, true);
    }

    @Bean
    public Queue locationUpdateQueue() {
        return new Queue(locationUpdateQueue, true);
    }

    @Bean
    public Queue locationPatchQueue() {
        return new Queue(locationPatchQueue, true);
    }




    @Bean
    public Binding locationCreateBinding() {
        return BindingBuilder
                .bind(locationCreateQueue())
                .to(locationExchange())
                .with(locationCreateRoutingKey);
    }

    @Bean
    public Binding locationUpdateBinding() {
        return BindingBuilder
                .bind(locationUpdateQueue())
                .to(locationExchange())
                .with(locationUpdateRoutingKey);
    }

    @Bean
    public Binding locationPatchBinding() {
        return BindingBuilder
                .bind(locationPatchQueue())
                .to(locationExchange())
                .with(locationPatchRoutingKey);
    }


}