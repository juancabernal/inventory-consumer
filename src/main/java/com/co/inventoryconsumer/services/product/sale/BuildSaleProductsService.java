package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.product.sale.SaleDetailDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BuildSaleProductsService {

    private final ExpandRecipeProductsService expandService;

    public BuildSaleProductsService(
            ExpandRecipeProductsService expandService
    ) {
        this.expandService = expandService;
    }

    public Map<UUID, BigDecimal> run(
            List<SaleDetailDTO> details
    ) {

        Map<UUID, BigDecimal> total = new HashMap<>();

        for (SaleDetailDTO detail : details) {

            Map<UUID, BigDecimal> partial =
                    expandService.run(
                            detail.getRecipeId(),
                            detail.getQuantity()
                    );

            if (partial == null) {
                return null;
            }

            partial.forEach((productId, quantity) ->
                    total.merge(
                            productId,
                            quantity,
                            BigDecimal::add
                    )
            );
        }

        return total;
    }
}