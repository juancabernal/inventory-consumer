package com.co.inventoryconsumer.dto.product.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDeleteResponseDTO {

    private UUID saleId;
    private boolean approved;
    private String message;
}