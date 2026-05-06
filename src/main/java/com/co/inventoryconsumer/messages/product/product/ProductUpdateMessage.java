package com.co.inventoryconsumer.messages.product.product;

import com.co.inventoryconsumer.dto.product.product.ProductUpdateDTO;
import com.co.inventoryconsumer.services.product.product.UpdateProductService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductUpdateMessage {

    private final UpdateProductService service;
    private final MapperJsonObjeto mapper;

    public ProductUpdateMessage(UpdateProductService service,
                                MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.update}")
    public void run(String messageJson) {

        ProductUpdateDTO request = mapToRequest(messageJson);

        service.run(request);
    }

    private ProductUpdateDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, ProductUpdateDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a ProductUpdateDTO")
                );
    }
}