package com.co.inventoryconsumer.dto.product.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaleDeleteRequestDTO {

    private SaleSnapshotDTO sale;
}