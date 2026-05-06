package com.co.inventoryconsumer.messages.product.product;

import com.co.inventoryconsumer.dto.product.product.ProductPatchMessageDTO;
import com.co.inventoryconsumer.services.product.product.PatchProductService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductPatchMessage {

    private final PatchProductService service;
    private final MapperJsonObjeto mapper;

    public ProductPatchMessage(PatchProductService service,
                               MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.patch}")
    public void run(String messageJson) {

        ProductPatchMessageDTO request = mapToRequest(messageJson);

        service.run(request);
    }

    private ProductPatchMessageDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, ProductPatchMessageDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a ProductPatchMessageDTO")
                );
    }
}