package com.co.inventoryconsumer.dto.product;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {

    private UUID idMessage;
    private boolean approved;
    private String message;

    private String sellerId;
    private String locationId;
    private String tableId;

    private List<SaleDetailResponseDTO> details;
}