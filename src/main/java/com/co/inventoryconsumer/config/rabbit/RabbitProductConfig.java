package com.co.inventoryconsumer.config.rabbit;

import com.co.inventoryconsumer.config.rabbit.AbstractRabbitConfig;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProductConfig extends AbstractRabbitConfig {

    @Value("${rabbitmq.exchange.product}")
    private String exchangeName;

    @Value("${rabbitmq.queue.product.create}")
    private String createQueue;

    @Value("${rabbitmq.queue.product.update}")
    private String updateQueue;

    @Value("${rabbitmq.queue.product.patch}")
    private String patchQueue;

    @Value("${rabbitmq.queue.product.delete}")
    private String deleteQueue;

    @Value("${rabbitmq.routing-key.product.create}")
    private String createKey;

    @Value("${rabbitmq.routing-key.product.update}")
    private String updateKey;

    @Value("${rabbitmq.routing-key.product.patch}")
    private String patchKey;

    @Value("${rabbitmq.routing-key.product.delete}")
    private String deleteKey;

    @Bean
    public DirectExchange productExchange() {
        return buildExchange(exchangeName);
    }

    @Bean
    public Queue productCreateQueue() {
        return buildQueue(createQueue);
    }

    @Bean
    public Queue productUpdateQueue() {
        return buildQueue(updateQueue);
    }

    @Bean
    public Queue productPatchQueue() {
        return buildQueue(patchQueue);
    }

    @Bean
    public Queue productDeleteQueue() {
        return buildQueue(deleteQueue);
    }

    @Bean
    public Binding productCreateBinding() {
        return buildBinding(productCreateQueue(), productExchange(), createKey);
    }

    @Bean
    public Binding productUpdateBinding() {
        return buildBinding(productUpdateQueue(), productExchange(), updateKey);
    }

    @Bean
    public Binding productPatchBinding() {
        return buildBinding(productPatchQueue(), productExchange(), patchKey);
    }

    @Bean
    public Binding productDeleteBinding() {
        return buildBinding(productDeleteQueue(), productExchange(), deleteKey);
    }
}