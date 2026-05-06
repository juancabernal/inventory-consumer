package com.co.inventoryconsumer.dto.product.product;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPatchMessageDTO {

    private UUID id;
    private ProductPatchDTO data;
}