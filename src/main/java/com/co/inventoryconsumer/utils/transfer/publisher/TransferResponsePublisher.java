package com.co.inventoryconsumer.utils.transfer.publisher;

import com.co.inventoryconsumer.dto.transfer.TransferResponseDTO;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransferResponsePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MapperJsonObjeto mapper;

    @Value("${rabbitmq.exchange.transfer.response:create.transfer.response.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.transfer.response:create.transfer.response}")
    private String routingKey;

    public TransferResponsePublisher(RabbitTemplate rabbitTemplate,
                                     MapperJsonObjeto mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void publish(TransferResponseDTO response) {
        String json = mapper.ejecutar(response)
                .orElseThrow(() -> new RuntimeException("ERROR: cannot serialize TransferResponseDTO"));

        rabbitTemplate.convertAndSend(exchange, routingKey, json);

        System.out.println("======================================");
        System.out.println("TRANSFER RESPONSE SENT TO RABBIT");
        System.out.println(json);
        System.out.println("======================================");
    }
}
