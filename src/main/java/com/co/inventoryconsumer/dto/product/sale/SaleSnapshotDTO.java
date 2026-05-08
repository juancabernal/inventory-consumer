package com.co.inventoryconsumer.dto.product.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleSnapshotDTO {

    private UUID id;

    private List<SaleDetailDTO> details;

    private UUID locationId;
    private String sellerId;
    private String tableId;
}