package com.co.inventoryconsumer.dto.product.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRecipeResponseDTO {

    private UUID recipeId;
    private String lineDisplayName;
    private String recipeLineComment;

    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    private boolean approved;
    private String message;
}
