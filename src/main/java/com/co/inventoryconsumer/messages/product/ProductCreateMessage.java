package com.co.inventoryconsumer.messages.product;

import com.co.inventoryconsumer.dto.product.ProductRequestDTO;
import com.co.inventoryconsumer.services.product.CreateProductService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductCreateMessage {

    private final CreateProductService productCreateService;
    private final MapperJsonObjeto mapper;

    public ProductCreateMessage(CreateProductService productCreateService,
                                MapperJsonObjeto mapper) {
        this.productCreateService = productCreateService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.product.create}")
    public void run(String messageJson) {

        printMessageReceived(messageJson);

        try {
            ProductRequestDTO request =
                    mapper.ejecutar(messageJson, ProductRequestDTO.class)
                            .orElseThrow(() ->
                                    new RuntimeException("No se pudo convertir a ProductRequestDTO")
                            );

            productCreateService.run(request);

            System.out.println("SUCCESS: Producto creado correctamente");

        } catch (Exception ex) {
            System.out.println("ERROR: Fallo al procesar creación de producto");
            System.out.println("Tipo: " + ex.getClass().getSimpleName());
            System.out.println("Mensaje: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void printMessageReceived(String messageJson) {
        System.out.println("======================================");
        System.out.println("PRODUCT CREATE MESSAGE RECIBIDO");
        System.out.println(messageJson);
        System.out.println("======================================");
    }
}