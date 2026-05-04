package com.co.inventoryconsumer.dto.transfer;

import com.co.inventoryconsumer.domain.transfer.TransferStatus;

public record TransferStatusUpdateDTO(
        Long idTraslado,
        String sedeOrigen,
        String sedeDestino,
        String observaciones,
        TransferStatus estado
) {
}
