package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculateRecipeCostService {

    private final CalculateProductsCostService productsCostService;
    private final CalculateSubRecipesCostService subRecipesCostService;

    public CalculateRecipeCostService(
            CalculateProductsCostService productsCostService,
            CalculateSubRecipesCostService subRecipesCostService
    ) {
        this.productsCostService = productsCostService;
        this.subRecipesCostService = subRecipesCostService;
    }

    public BigDecimal run(RecipeRequest request) {

        return productsCostService.run(request)
                .add(
                        subRecipesCostService.run(request)
                );
    }
}