package com.co.inventoryconsumer.messages.transfer;

import com.co.inventoryconsumer.dto.transfer.TransferResponseDTO;
import com.co.inventoryconsumer.dto.transfer.TransferStatusUpdateDTO;
import com.co.inventoryconsumer.services.transfer.TransferService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransferStatusMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferStatusMessage.class);
    private static final String LOG_SEPARATOR = "======================================";

    private final TransferService transferService;
    private final MapperJsonObjeto mapper;

    public TransferStatusMessage(TransferService transferService,
                                 MapperJsonObjeto mapper) {
        this.transferService = transferService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.transfer.status.request:update.transfer.status.request.queue}")
    public void receiveTransferStatusUpdate(String messageJson) {
        LOGGER.info(LOG_SEPARATOR);
        LOGGER.info("TRANSFER STATUS UPDATE RECEIVED");
        LOGGER.info("{}", messageJson);
        LOGGER.info(LOG_SEPARATOR);

        try {
            Optional<TransferStatusUpdateDTO> requestOpt =
                    mapper.ejecutar(messageJson, TransferStatusUpdateDTO.class);

            if (requestOpt.isEmpty()) {
                LOGGER.warn("TRANSFER STATUS ERROR: message could not be mapped");
                return;
            }

            TransferResponseDTO response = transferService.updateTransferStatus(requestOpt.get());
            transferService.publishResponse(response);
        } catch (Exception e) {
            LOGGER.error("TRANSFER STATUS EXCEPTION", e);
        }
    }
}
