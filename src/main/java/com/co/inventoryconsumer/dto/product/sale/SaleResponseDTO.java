package com.co.inventoryconsumer.dto.product.sale;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {

    private UUID idMessage;

    private UUID locationId;
    private String sellerId;
    private String tableId;

    private List<SaleRecipeResponseDTO> recipes;
}