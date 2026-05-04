package com.co.inventoryconsumer.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailResponseDTO {

    private UUID recipeId;
    private String lineDisplayName;
    private boolean approved;
    private String reason;
}