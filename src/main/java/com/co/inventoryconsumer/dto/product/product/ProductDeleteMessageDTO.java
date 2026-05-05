package com.co.inventoryconsumer.dto.product.product;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDeleteMessageDTO {

    private UUID id;
}