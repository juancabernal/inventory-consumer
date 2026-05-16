package com.co.inventoryconsumer.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitCategoryConfig extends AbstractRabbitConfig {

    @Value("${rabbitmq.exchange.category}")
    private String exchangeName;

    @Value("${rabbitmq.exchange.category.dlx}")
    private String deadLetterExchangeName;

    @Value("${rabbitmq.queue.category.create}")
    private String createQueue;

    @Value("${rabbitmq.queue.category.update-status}")
    private String updateStatusQueue;

    @Value("${rabbitmq.queue.category.create.dlq}")
    private String createDeadLetterQueue;

    @Value("${rabbitmq.queue.category.update-status.dlq}")
    private String updateStatusDeadLetterQueue;

    @Value("${rabbitmq.routing-key.category.create}")
    private String createKey;

    @Value("${rabbitmq.routing-key.category.update-status}")
    private String updateStatusKey;

    @Value("${rabbitmq.routing-key.category.create.dlq}")
    private String createDeadLetterKey;

    @Value("${rabbitmq.routing-key.category.update-status.dlq}")
    private String updateStatusDeadLetterKey;

    @Bean
    public DirectExchange categoryExchange() {
        return buildExchange(exchangeName);
    }

    @Bean
    public DirectExchange categoryDeadLetterExchange() {
        return buildExchange(deadLetterExchangeName);
    }

    @Bean
    public Queue categoryCreateQueue() {
        return QueueBuilder.durable(createQueue)
                .deadLetterExchange(deadLetterExchangeName)
                .deadLetterRoutingKey(createDeadLetterKey)
                .build();
    }

    @Bean
    public Queue categoryUpdateStatusQueue() {
        return QueueBuilder.durable(updateStatusQueue)
                .deadLetterExchange(deadLetterExchangeName)
                .deadLetterRoutingKey(updateStatusDeadLetterKey)
                .build();
    }

    @Bean
    public Queue categoryCreateDeadLetterQueue() {
        return buildQueue(createDeadLetterQueue);
    }

    @Bean
    public Queue categoryUpdateStatusDeadLetterQueue() {
        return buildQueue(updateStatusDeadLetterQueue);
    }

    @Bean
    public Binding categoryCreateBinding() {
        return buildBinding(categoryCreateQueue(), categoryExchange(), createKey);
    }

    @Bean
    public Binding categoryUpdateStatusBinding() {
        return buildBinding(categoryUpdateStatusQueue(), categoryExchange(), updateStatusKey);
    }

    @Bean
    public Binding categoryCreateDeadLetterBinding() {
        return BindingBuilder
                .bind(categoryCreateDeadLetterQueue())
                .to(categoryDeadLetterExchange())
                .with(createDeadLetterKey);
    }

    @Bean
    public Binding categoryUpdateStatusDeadLetterBinding() {
        return BindingBuilder
                .bind(categoryUpdateStatusDeadLetterQueue())
                .to(categoryDeadLetterExchange())
                .with(updateStatusDeadLetterKey);
    }
}
