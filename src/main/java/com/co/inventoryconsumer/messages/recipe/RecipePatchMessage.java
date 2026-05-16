package com.co.inventoryconsumer.messages.recipe;

import com.co.inventoryconsumer.services.recipe.DeleteRecipeService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RecipePatchMessage {

    private static final Logger log = LoggerFactory.getLogger(RecipePatchMessage.class);

    private final DeleteRecipeService service;
    private final MapperJsonObjeto mapper;

    public RecipePatchMessage(DeleteRecipeService service, MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    @RabbitListener(queues = "${rabbitmq.queue.recipe.patch}")
    public void run(String messageJson) {
        log.info("[recipe.patch] Mensaje recibido: {}", messageJson);
        try {
            Map<String, String> payload = mapper.ejecutar(messageJson, Map.class)
                    .map(m -> (Map<String, String>) m)
                    .orElseThrow(() -> new IllegalArgumentException("JSON inválido para inactivación: " + messageJson));

            String name = payload.get("name");
            if (name == null || name.isBlank()) {
                log.error("[recipe.patch] Mensaje descartado: falta el campo 'name'");
                return;
            }

            service.run(name);
            log.info("[recipe.patch] Receta '{}' inactivada correctamente", name);

        } catch (IllegalArgumentException e) {
            log.error("[recipe.patch] Mensaje inválido descartado: {}", e.getMessage());
        } catch (Exception e) {
            log.error("[recipe.patch] Error al inactivar '{}': {} - {}",
                    messageJson, e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
