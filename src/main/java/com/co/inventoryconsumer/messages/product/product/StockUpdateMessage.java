package com.co.inventoryconsumer.messages.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.product.StockUpdateDTO;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.services.product.product.IncreaseProductStockService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class StockUpdateMessage {

    private final IncreaseProductStockService service;
    private final ProductRepository repo;
    private final MapperJsonObjeto mapper;

    public StockUpdateMessage(
            IncreaseProductStockService service,
            ProductRepository repo,
            MapperJsonObjeto mapper
    ) {
        this.service = service;
        this.repo = repo;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.stock}")
    public void run(String messageJson) {

        StockUpdateDTO dto =
                mapper.ejecutar(
                        messageJson,
                        StockUpdateDTO.class
                ).orElseThrow(() ->
                        new BusinessException(
                                "No se pudo convertir el mensaje a StockUpdateDTO"
                        )
                );

        Product product =
                repo.findById(dto.getProductId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Producto no encontrado"
                                )
                        );

        Map<UUID, Product> products =
                new HashMap<>();

        products.put(
                product.getId(),
                product
        );

        Map<UUID, BigDecimal> quantities =
                new HashMap<>();

        quantities.put(
                product.getId(),
                dto.getQuantity()
        );

        service.run(
                products,
                quantities
        );
    }
}