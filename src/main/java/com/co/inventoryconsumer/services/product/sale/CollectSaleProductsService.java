package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.product.sale.SaleDetailDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CollectSaleProductsService {

    private final BuildSaleProductsService buildService;

    public CollectSaleProductsService(
            BuildSaleProductsService buildService
    ) {
        this.buildService = buildService;
    }

    public Map<UUID, BigDecimal> run(
            List<SaleDetailDTO> details
    ) {
        return buildService.run(details);
    }
}