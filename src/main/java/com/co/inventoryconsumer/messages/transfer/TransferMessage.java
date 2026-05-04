package com.co.inventoryconsumer.messages.transfer;

import com.co.inventoryconsumer.dto.transfer.TransferRequestDTO;
import com.co.inventoryconsumer.dto.transfer.TransferResponseDTO;
import com.co.inventoryconsumer.services.transfer.TransferService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransferMessage {

    private final TransferService transferService;
    private final MapperJsonObjeto mapper;

    public TransferMessage(TransferService transferService,
                           MapperJsonObjeto mapper) {
        this.transferService = transferService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.transfer.request:create.transfer.request.queue}")
    public void receiveTransfer(String messageJson) {
        logIncomingMessage(messageJson);

        try {
            Optional<TransferRequestDTO> requestOpt =
                    mapper.ejecutar(messageJson, TransferRequestDTO.class);

            if (requestOpt.isEmpty()) {
                logError("Transfer message could not be mapped");
                return;
            }

            TransferResponseDTO response = transferService.processTransfer(requestOpt.get());
            transferService.publishResponse(response);
        } catch (Exception e) {
            logException(e);
        }
    }

    private void logIncomingMessage(String messageJson) {
        System.out.println("======================================");
        System.out.println("TRANSFER REQUEST RECEIVED");
        System.out.println(messageJson);
        System.out.println("======================================");
    }

    private void logError(String message) {
        System.out.println("TRANSFER ERROR: " + message);
    }

    private void logException(Exception e) {
        System.out.println("TRANSFER EXCEPTION");
        e.printStackTrace();
    }
}
