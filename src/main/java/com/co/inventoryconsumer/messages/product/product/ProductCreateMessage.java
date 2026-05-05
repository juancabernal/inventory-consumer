package com.co.inventoryconsumer.messages.product.product;

import com.co.inventoryconsumer.dto.product.product.ProductRequestDTO;
import com.co.inventoryconsumer.services.product.product.CreateProductService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductCreateMessage {

    private final CreateProductService service;
    private final MapperJsonObjeto mapper;

    public ProductCreateMessage(CreateProductService service,
                                MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.create}")
    public void run(String messageJson) {

        ProductRequestDTO request = mapToRequest(messageJson);

        service.run(request);
    }

    private ProductRequestDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, ProductRequestDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a ProductRequestDTO")
                );
    }
}