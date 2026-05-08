package com.co.inventoryconsumer.messages.product.sale;

import com.co.inventoryconsumer.dto.product.sale.ProductSaleUpdateRequestDTO;
import com.co.inventoryconsumer.dto.product.sale.SaleUpdateResponseDTO;
import com.co.inventoryconsumer.services.product.sale.ValidateProductForSaleUpdateService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import com.co.inventoryconsumer.utils.product.publisher.RabbitPublisher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductSaleUpdateMessage {

    private final ValidateProductForSaleUpdateService service;
    private final MapperJsonObjeto mapper;
    private final RabbitPublisher publisher;

    @Value("${rabbitmq.exchange.sales.update.response}")
    private String exchange;

    @Value("${rabbitmq.routing-key.sales.update.response}")
    private String routingKey;

    public ProductSaleUpdateMessage(
            ValidateProductForSaleUpdateService service,
            MapperJsonObjeto mapper,
            RabbitPublisher publisher
    ) {
        this.service = service;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @RabbitListener(
            queues = "${rabbitmq.queue.sales.update.request}"
    )
    public void run(String messageJson) {

        ProductSaleUpdateRequestDTO request =
                mapToRequest(messageJson);

        SaleUpdateResponseDTO response =
                service.run(request);

        publisher.publish(
                exchange,
                routingKey,
                response
        );
    }

    private ProductSaleUpdateRequestDTO mapToRequest(
            String messageJson
    ) {

        return mapper.ejecutar(
                messageJson,
                ProductSaleUpdateRequestDTO.class
        ).orElseThrow(() ->
                new BusinessException(
                        "No se pudo convertir el mensaje update"
                )
        );
    }
}