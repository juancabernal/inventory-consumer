package com.co.inventoryconsumer.dto.product.sale;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleUpdateResponseDTO {

    private UUID saleId;
    private SaleResponseDTO sale;
}