package com.co.inventoryconsumer.messages.product.product;

import com.co.inventoryconsumer.dto.product.product.StockUpdateDTO;
import com.co.inventoryconsumer.services.product.product.IncreaseProductStockService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockUpdateMessage {

    private final IncreaseProductStockService service;
    private final MapperJsonObjeto mapper;

    public StockUpdateMessage(IncreaseProductStockService service,
                              MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.stock}")
    public void run(String messageJson) {
        StockUpdateDTO dto = mapper.ejecutar(messageJson, StockUpdateDTO.class)
                .orElseThrow(() -> new BusinessException("No se pudo convertir el mensaje a StockUpdateDTO"));
        service.run(dto);
    }
}