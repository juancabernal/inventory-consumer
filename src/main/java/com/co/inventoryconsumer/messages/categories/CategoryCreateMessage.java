package com.co.inventoryconsumer.messages.categories;

import com.co.inventoryconsumer.dto.categories.CategoryRequestDTO;
import com.co.inventoryconsumer.services.categories.CreateCategoryService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryCreateMessage {

    private final CreateCategoryService service;
    private final MapperJsonObjeto mapper;

    public CategoryCreateMessage(CreateCategoryService service,
                                 MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.category.create}")
    public void run(String messageJson) {
        try {
            CategoryRequestDTO request = mapToRequest(messageJson);

            service.run(request);
        } catch (Exception exception) {
            throw new AmqpRejectAndDontRequeueException("No se pudo procesar el mensaje de creacion de categoria", exception);
        }
    }

    private CategoryRequestDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, CategoryRequestDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a CategoryRequestDTO")
                );
    }
}
