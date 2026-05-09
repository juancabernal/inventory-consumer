package com.co.inventoryconsumer.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitCategoryConfig extends AbstractRabbitConfig {

    @Value("${rabbitmq.exchange.category}")
    private String exchangeName;

    @Value("${rabbitmq.queue.category.create}")
    private String createQueue;

    @Value("${rabbitmq.queue.category.update-status}")
    private String updateStatusQueue;

    @Value("${rabbitmq.routing-key.category.create}")
    private String createKey;

    @Value("${rabbitmq.routing-key.category.update-status}")
    private String updateStatusKey;

    @Bean
    public DirectExchange categoryExchange() {
        return buildExchange(exchangeName);
    }

    @Bean
    public Queue categoryCreateQueue() {
        return buildQueue(createQueue);
    }

    @Bean
    public Queue categoryUpdateStatusQueue() {
        return buildQueue(updateStatusQueue);
    }

    @Bean
    public Binding categoryCreateBinding() {
        return buildBinding(categoryCreateQueue(), categoryExchange(), createKey);
    }

    @Bean
    public Binding categoryUpdateStatusBinding() {
        return buildBinding(categoryUpdateStatusQueue(), categoryExchange(), updateStatusKey);
    }
}
