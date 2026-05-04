package com.co.inventoryconsumer.utils.product.publisher;

import com.co.inventoryconsumer.dto.product.SaleResponseDTO;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SaleResponsePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MapperJsonObjeto mapper;

    @Value("${rabbitmq.exchange.sales.response}")
    private String exchange;

    @Value("${rabbitmq.routing-key.sales.response}")
    private String routingKey;

    public SaleResponsePublisher(RabbitTemplate rabbitTemplate,
                                 MapperJsonObjeto mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void publish(SaleResponseDTO response) {

        System.out.println("======================================");
        System.out.println("START SERIALIZATION");
        System.out.println("RESPONSE OBJECT: " + response);
        System.out.println("======================================");

        String json = mapper.ejecutar(response)
                .orElseThrow(() -> {

                    System.out.println("======================================");
                    System.out.println("SERIALIZATION FAILED");
                    System.out.println("OBJECT: " + response);
                    System.out.println("======================================");

                    return new RuntimeException("ERROR: cannot serialize SaleResponseDTO");
                });

        System.out.println("SERIALIZATION OK:");
        System.out.println(json);

        rabbitTemplate.convertAndSend(exchange, routingKey, json);

        logSent(json);
    }

    private void logSent(String json) {
        System.out.println("======================================");
        System.out.println("SALE RESPONSE SENT TO RABBIT");
        System.out.println(json);
        System.out.println("======================================");
    }
}