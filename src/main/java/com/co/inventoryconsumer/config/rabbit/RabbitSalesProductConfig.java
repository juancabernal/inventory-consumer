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

    @Value("${rabbitmq.exchange.sales.update.request}")
    private String updateRequestExchangeName;

    @Value("${rabbitmq.exchange.sales.update.response}")
    private String updateResponseExchangeName;

    @Value("${rabbitmq.queue.sales.update.request}")
    private String updateRequestQueue;

    @Value("${rabbitmq.queue.sales.update.response}")
    private String updateResponseQueue;

    @Value("${rabbitmq.routing-key.sales.update.request}")
    private String updateRequestKey;

    @Value("${rabbitmq.routing-key.sales.update.response}")
    private String updateResponseKey;

    @Value("${rabbitmq.exchange.sales.delete.request}")
    private String deleteRequestExchangeName;

    @Value("${rabbitmq.exchange.sales.delete.response}")
    private String deleteResponseExchangeName;

    @Value("${rabbitmq.queue.sales.delete.request}")
    private String deleteRequestQueue;

    @Value("${rabbitmq.queue.sales.delete.response}")
    private String deleteResponseQueue;

    @Value("${rabbitmq.routing-key.sales.delete.request}")
    private String deleteRequestKey;

    @Value("${rabbitmq.routing-key.sales.delete.response}")
    private String deleteResponseKey;

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

    @Bean
    public DirectExchange salesUpdateRequestExchange() {
        return buildExchange(updateRequestExchangeName);
    }

    @Bean
    public DirectExchange salesUpdateResponseExchange() {
        return buildExchange(updateResponseExchangeName);
    }

    @Bean
    public Queue salesUpdateRequestQueue() {
        return buildQueue(updateRequestQueue);
    }

    @Bean
    public Queue salesUpdateResponseQueue() {
        return buildQueue(updateResponseQueue);
    }

    @Bean
    public Binding salesUpdateRequestBinding() {
        return buildBinding(
                salesUpdateRequestQueue(),
                salesUpdateRequestExchange(),
                updateRequestKey
        );
    }

    @Bean
    public Binding salesUpdateResponseBinding() {
        return buildBinding(
                salesUpdateResponseQueue(),
                salesUpdateResponseExchange(),
                updateResponseKey
        );
    }

    @Bean
    public DirectExchange salesDeleteRequestExchange() {
        return buildExchange(deleteRequestExchangeName);
    }

    @Bean
    public DirectExchange salesDeleteResponseExchange() {
        return buildExchange(deleteResponseExchangeName);
    }

    @Bean
    public Queue salesDeleteRequestQueue() {
        return buildQueue(deleteRequestQueue);
    }

    @Bean
    public Queue salesDeleteResponseQueue() {
        return buildQueue(deleteResponseQueue);
    }

    @Bean
    public Binding salesDeleteRequestBinding() {
        return buildBinding(
                salesDeleteRequestQueue(),
                salesDeleteRequestExchange(),
                deleteRequestKey
        );
    }

    @Bean
    public Binding salesDeleteResponseBinding() {
        return buildBinding(
                salesDeleteResponseQueue(),
                salesDeleteResponseExchange(),
                deleteResponseKey
        );
    }
}