package com.co.inventoryconsumer.messages.product;

import com.co.inventoryconsumer.dto.product.ProductSaleRequestDTO;
import com.co.inventoryconsumer.dto.product.SaleResponseDTO;
import com.co.inventoryconsumer.services.product.ProductSaleService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductSaleMessage {

    private final ProductSaleService service;
    private final MapperJsonObjeto mapper;

    public ProductSaleMessage(ProductSaleService service,
                              MapperJsonObjeto mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.sales.create.request}")
    public void receiveSale(String messageJson) {

        System.out.println("======================================");
        System.out.println("RAW MESSAGE RECEIVED");
        System.out.println(messageJson);
        System.out.println("======================================");

        try {

            System.out.println("TRYING TO MAP JSON -> DTO");

            Optional<ProductSaleRequestDTO> requestOpt =
                    mapper.ejecutar(messageJson, ProductSaleRequestDTO.class);

            if (requestOpt.isEmpty()) {

                System.out.println("======================================");
                System.out.println("MAPPING FAILED");
                System.out.println("JSON:");
                System.out.println(messageJson);
                System.out.println("======================================");

                return;
            }

            ProductSaleRequestDTO request = requestOpt.get();

            System.out.println("MAPPING SUCCESS");
            System.out.println(request);

            SaleResponseDTO response = service.processSale(request);

            service.publishResponse(response);

        } catch (Exception e) {

            System.out.println("EXCEPTION DURING CONSUME");
            e.printStackTrace();
        }
    }

    private void logIncomingMessage(String messageJson) {
        System.out.println("======================================");
        System.out.println("SALE CREATE REQUEST RECEIVED");
        System.out.println(messageJson);
        System.out.println("======================================");
    }

    private void logError(String message) {
        System.out.println("ERROR: " + message);
    }

    private void logException(Exception e) {
        System.out.println("ERROR PROCESSING SALE");
        e.printStackTrace();
    }
}