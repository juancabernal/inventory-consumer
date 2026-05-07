package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.product.sale.SaleDetailDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CollectSaleProductsService {

    private final ExpandRecipeProductsService expandService;

    public CollectSaleProductsService(ExpandRecipeProductsService expandService) {
        this.expandService = expandService;
    }

    public Map<UUID, BigDecimal> run(List<SaleDetailDTO> details) {

        Map<UUID, BigDecimal> total = new HashMap<>();

        for (SaleDetailDTO detail : details) {

            Map<UUID, BigDecimal> partial =
                    expandService.run(
                            detail.getRecipeId(),
                            detail.getQuantity()
                    );

            partial.forEach((productId, qty) ->
                    total.merge(productId, qty, BigDecimal::add)
            );
        }

        return total;
    }
}