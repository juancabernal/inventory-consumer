package com.co.inventoryconsumer.messages.product.sale;

import com.co.inventoryconsumer.dto.product.sale.ProductSaleRequestDTO;
import com.co.inventoryconsumer.dto.product.sale.SaleResponseDTO;
import com.co.inventoryconsumer.services.product.sale.ValidateProductForSaleCreateService;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import com.co.inventoryconsumer.utils.product.publisher.RabbitPublisher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductSaleCreateMessage {

    private final ValidateProductForSaleCreateService service;
    private final MapperJsonObjeto mapper;
    private final RabbitPublisher publisher;

    @Value("${rabbitmq.exchange.sales.create.response}")
    private String exchange;

    @Value("${rabbitmq.routing-key.sales.create.response}")
    private String routingKey;

    public ProductSaleCreateMessage(ValidateProductForSaleCreateService service,
                                    MapperJsonObjeto mapper,
                                    RabbitPublisher publisher) {
        this.service = service;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @RabbitListener(queues = "${rabbitmq.queue.sales.create.request}")
    public void run(String messageJson) {

        ProductSaleRequestDTO request = mapToRequest(messageJson);

        SaleResponseDTO response = service.run(request);

        publisher.publish(exchange, routingKey, response);
    }

    private ProductSaleRequestDTO mapToRequest(String messageJson) {
        return mapper.ejecutar(messageJson, ProductSaleRequestDTO.class)
                .orElseThrow(() ->
                        new BusinessException("No se pudo convertir el mensaje a ProductSaleRequestDTO")
                );
    }
}