package com.co.inventoryconsumer.messages.recipe;

import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import com.co.inventoryconsumer.services.recipe.UpdateRecipeService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RecipeUpdateMessage {

    private static final Logger log = LoggerFactory.getLogger(RecipeUpdateMessage.class);

    private final UpdateRecipeService service;
    private final MapperJsonObjeto mapper;

    public RecipeUpdateMessage(UpdateRecipeService service, MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.recipe.update}")
    public void run(String messageJson) {
        log.info("[recipe.update] Mensaje recibido: {}", messageJson);
        try {
            RecipeRequest request = mapper.ejecutar(messageJson, RecipeRequest.class)
                    .orElseThrow(() -> new IllegalArgumentException("JSON no corresponde a RecipeRequest: " + messageJson));

            service.run(request);
            log.info("[recipe.update] Receta '{}' actualizada correctamente", request.getName());

        } catch (IllegalArgumentException e) {
            log.error("[recipe.update] Mensaje inválido descartado: {}", e.getMessage());
        } catch (Exception e) {
            log.error("[recipe.update] Error al procesar '{}': {} - {}",
                    messageJson, e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
