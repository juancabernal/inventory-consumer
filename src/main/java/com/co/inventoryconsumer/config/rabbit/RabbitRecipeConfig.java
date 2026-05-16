package com.co.inventoryconsumer.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitRecipeConfig extends AbstractRabbitConfig {

    @Value("${rabbitmq.exchange.recipe}")
    private String exchangeName;

    @Value("${rabbitmq.queue.recipe.create}")
    private String createQueue;

    @Value("${rabbitmq.queue.recipe.update}")
    private String updateQueue;

    @Value("${rabbitmq.queue.recipe.patch}")
    private String patchQueue;

    @Value("${rabbitmq.routing-key.recipe.create}")
    private String createKey;

    @Value("${rabbitmq.routing-key.recipe.update}")
    private String updateKey;

    @Value("${rabbitmq.routing-key.recipe.patch}")
    private String patchKey;

    @Bean
    public DirectExchange recipeExchange() {
        return buildExchange(exchangeName);
    }

    @Bean
    public Queue recipeCreateQueue() {
        return buildQueue(createQueue);
    }

    @Bean
    public Queue recipeUpdateQueue() {
        return buildQueue(updateQueue);
    }

    @Bean
    public Queue recipePatchQueue() {
        return buildQueue(patchQueue);
    }

    @Bean
    public Binding recipeCreateBinding() {
        return buildBinding(recipeCreateQueue(), recipeExchange(), createKey);
    }

    @Bean
    public Binding recipeUpdateBinding() {
        return buildBinding(recipeUpdateQueue(), recipeExchange(), updateKey);
    }

    @Bean
    public Binding recipePatchBinding() {
        return buildBinding(recipePatchQueue(), recipeExchange(), patchKey);
    }
}
