package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.recipe.RecipeResponse;
import com.co.inventoryconsumer.services.recipe.GetRecipeByIdService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ExpandRecipeProductsService {

    private final GetRecipeByIdService getRecipeByIdService;

    public ExpandRecipeProductsService(
            GetRecipeByIdService getRecipeByIdService
    ) {
        this.getRecipeByIdService = getRecipeByIdService;
    }

    public Map<UUID, BigDecimal> run(
            UUID recipeId,
            BigDecimal multiplier
    ) {

        Map<UUID, BigDecimal> accumulator = new HashMap<>();

        boolean success = process(
                recipeId,
                multiplier,
                accumulator,
                new HashSet<>()
        );

        if (!success) {
            return null;
        }

        return accumulator;
    }

    private boolean process(
            UUID recipeId,
            BigDecimal multiplier,
            Map<UUID, BigDecimal> acc,
            Set<UUID> visited
    ) {

        if (visited.contains(recipeId)) {
            return false;
        }

        visited.add(recipeId);

        RecipeResponse recipe =
                getRecipeByIdService.run(recipeId);

        if (recipe == null) {
            return false;
        }

        if (recipe.getProducts() != null) {

            recipe.getProducts().forEach(product -> {

                BigDecimal quantity =
                        BigDecimal.valueOf(product.getQuantity())
                                .multiply(multiplier);

                acc.merge(
                        product.getProductId(),
                        quantity,
                        BigDecimal::add
                );
            });
        }

        if (recipe.getSubRecipes() != null) {

            for (var sub : recipe.getSubRecipes()) {

                BigDecimal newMultiplier =
                        multiplier.multiply(
                                BigDecimal.valueOf(sub.getQuantity())
                        );

                boolean success = process(
                        sub.getSubRecipeId(),
                        newMultiplier,
                        acc,
                        visited
                );

                if (!success) {
                    return false;
                }
            }
        }

        visited.remove(recipeId);

        return true;
    }
}