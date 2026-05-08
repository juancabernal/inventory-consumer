package com.co.inventoryconsumer.dto.product.product;

import java.math.BigDecimal;
import java.util.UUID;

public class StockUpdateDTO {
    private UUID locationId;
    private String productId;
    private BigDecimal quantity;

    public UUID getLocationId() { return locationId; }
    public String getProductId() { return productId; }
    public BigDecimal getQuantity() { return quantity; }
}