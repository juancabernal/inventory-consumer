package com.co.inventoryconsumer.messages.product.product;

import com.co.inventoryconsumer.dto.product.product.ProductDeleteMessageDTO;
import com.co.inventoryconsumer.services.product.product.DeleteProductService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductDeleteMessage {

    private final DeleteProductService service;
    private final MapperJsonObjeto mapper;

    public ProductDeleteMessage(DeleteProductService service,
                                MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.delete}")
    public void run(String messageJson) {

        ProductDeleteMessageDTO request = mapToRequest(messageJson);

        service.run(request);
    }

    private ProductDeleteMessageDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, ProductDeleteMessageDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a ProductDeleteMessageDTO")
                );
    }
}