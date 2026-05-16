package com.co.inventoryconsumer.messages.categories;

import com.co.inventoryconsumer.dto.categories.CategoryUpdateStatusDTO;
import com.co.inventoryconsumer.services.categories.UpdateCategoryStatusService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdateStatusMessage {

    private final UpdateCategoryStatusService service;
    private final MapperJsonObjeto mapper;

    public CategoryUpdateStatusMessage(UpdateCategoryStatusService service,
                                       MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.category.update-status}")
    public void run(String messageJson) {
        try {
            CategoryUpdateStatusDTO request = mapToRequest(messageJson);

            service.run(request.getId(), request.getData());
        } catch (Exception exception) {
            throw new AmqpRejectAndDontRequeueException("No se pudo procesar el mensaje de actualizacion de estado de categoria", exception);
        }
    }

    private CategoryUpdateStatusDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, CategoryUpdateStatusDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a CategoryUpdateStatusDTO")
                );
    }
}
