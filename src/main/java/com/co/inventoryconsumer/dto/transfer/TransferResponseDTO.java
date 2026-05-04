package com.co.inventoryconsumer.dto.transfer;

import com.co.inventoryconsumer.domain.transfer.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDTO(
        Long idTraslado,
        String sedeOrigen,
        String sedeDestino,
        LocalDateTime fechaEnvio,
        LocalDateTime fechaLlegada,
        String responsable,
        String producto,
        BigDecimal stock,
        Integer cantidad,
        String observaciones,
        TransferStatus estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
