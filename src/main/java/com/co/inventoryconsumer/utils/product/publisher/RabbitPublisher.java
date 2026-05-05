package com.co.inventoryconsumer.utils.product.publisher;

import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MapperJsonObjeto mapper;

    public RabbitPublisher(RabbitTemplate rabbitTemplate,
                           MapperJsonObjeto mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void publish(String exchange,
                        String routingKey,
                        Object payload) {

        String json = mapper.ejecutar(payload)
                .orElseThrow(() ->
                        new BusinessException("No se pudo serializar el mensaje a JSON")
                );

        rabbitTemplate.convertAndSend(exchange, routingKey, json);
    }
}