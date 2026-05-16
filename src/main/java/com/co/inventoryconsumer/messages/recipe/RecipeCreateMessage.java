package com.co.inventoryconsumer.messages.recipe;

import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import com.co.inventoryconsumer.services.recipe.CreateRecipeService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RecipeCreateMessage {

    private static final Logger log = LoggerFactory.getLogger(RecipeCreateMessage.class);

    private final CreateRecipeService service;
    private final MapperJsonObjeto mapper;

    public RecipeCreateMessage(CreateRecipeService service, MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.recipe.create}")
    public void run(String messageJson) {
        log.info("[recipe.create] Mensaje recibido: {}", messageJson);
        try {
            RecipeRequest request = mapper.ejecutar(messageJson, RecipeRequest.class)
                    .orElseThrow(() -> new IllegalArgumentException("JSON no corresponde a RecipeRequest: " + messageJson));

            service.run(request);
            log.info("[recipe.create] Receta '{}' creada correctamente", request.getName());

        } catch (IllegalArgumentException e) {
            log.error("[recipe.create] Mensaje inválido descartado: {}", e.getMessage());
        } catch (Exception e) {
            log.error("[recipe.create] Error al procesar '{}': {} - {}",
                    messageJson, e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
