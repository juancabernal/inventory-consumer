package com.co.inventoryconsumer.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailDTO {

    private UUID recipeId;
    private String lineDisplayName;
    private String recipeLineComment;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}