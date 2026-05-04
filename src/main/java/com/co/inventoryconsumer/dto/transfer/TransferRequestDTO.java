package com.co.inventoryconsumer.dto.transfer;

import java.time.LocalDateTime;

public record TransferRequestDTO(
        String sedeOrigen,
        String sedeDestino,
        LocalDateTime fechaEnvio,
        LocalDateTime fechaLlegada,
        String responsable,
        String producto,
        Integer cantidad,
        String observaciones
) {
}
