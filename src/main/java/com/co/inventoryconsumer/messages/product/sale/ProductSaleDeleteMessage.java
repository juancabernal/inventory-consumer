package com.co.inventoryconsumer.messages.product.sale;

import com.co.inventoryconsumer.dto.product.sale.ProductSaleDeleteRequestDTO;
import com.co.inventoryconsumer.dto.product.sale.SaleDeleteResponseDTO;
import com.co.inventoryconsumer.services.product.sale.ValidateProductForSaleDeleteService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import com.co.inventoryconsumer.utils.product.publisher.RabbitPublisher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductSaleDeleteMessage {

    private final ValidateProductForSaleDeleteService service;
    private final MapperJsonObjeto mapper;
    private final RabbitPublisher publisher;

    @Value("${rabbitmq.exchange.sales.delete.response}")
    private String exchange;

    @Value("${rabbitmq.routing-key.sales.delete.response}")
    private String routingKey;

    public ProductSaleDeleteMessage(
            ValidateProductForSaleDeleteService service,
            MapperJsonObjeto mapper,
            RabbitPublisher publisher
    ) {
        this.service = service;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @RabbitListener(
            queues = "${rabbitmq.queue.sales.delete.request}"
    )
    public void run(String messageJson) {

        ProductSaleDeleteRequestDTO request =
                mapToRequest(messageJson);

        SaleDeleteResponseDTO response =
                service.run(request);

        publisher.publish(
                exchange,
                routingKey,
                response
        );
    }

    private ProductSaleDeleteRequestDTO mapToRequest(
            String messageJson
    ) {

        return mapper.ejecutar(
                messageJson,
                ProductSaleDeleteRequestDTO.class
        ).orElseThrow(() ->
                new BusinessException(
                        "No se pudo convertir el mensaje delete"
                )
        );
    }
}