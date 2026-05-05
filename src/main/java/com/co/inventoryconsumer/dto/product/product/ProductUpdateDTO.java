package com.co.inventoryconsumer.dto.product.product;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

    private UUID id;
    private ProductRequestDTO data;
}