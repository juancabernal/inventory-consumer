package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.recipe.RecipeResponse;
import com.co.inventoryconsumer.dto.recipe.RecipeSubRecipeRequest;
import com.co.inventoryconsumer.services.recipe.GetRecipeByIdService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ExpandRecipeProductsService {

    private final GetRecipeByIdService getRecipeByIdService;

    public ExpandRecipeProductsService(GetRecipeByIdService getRecipeByIdService) {
        this.getRecipeByIdService = getRecipeByIdService;
    }

    public Map<UUID, BigDecimal> run(UUID recipeId, BigDecimal multiplier) {

        Map<UUID, BigDecimal> accumulator = new HashMap<>();

        process(recipeId, multiplier, accumulator, new HashSet<>());

        return accumulator;
    }

    private void process(UUID recipeId,
                         BigDecimal multiplier,
                         Map<UUID, BigDecimal> acc,
                         Set<UUID> visited) {

        if (visited.contains(recipeId)) {
            throw new RuntimeException("Recursividad circular en receta: " + recipeId);
        }

        visited.add(recipeId);

        RecipeResponse recipe = getRecipeByIdService.run(recipeId);

        if (recipe == null) {
            throw new RuntimeException("RECIPE_NOT_FOUND");
        }

        if (recipe.getProducts() != null) {
            recipe.getProducts().forEach(p -> {

                BigDecimal qty = BigDecimal.valueOf(p.getQuantity())
                        .multiply(multiplier);

                acc.merge(
                        p.getProductId(),
                        qty,
                        BigDecimal::add
                );
            });
        }

        if (recipe.getSubRecipes() != null) {
            for (var sub : recipe.getSubRecipes()) {

                BigDecimal newMultiplier = multiplier.multiply(
                        BigDecimal.valueOf(sub.getQuantity())
                );

                process(
                        sub.getSubRecipeId(),
                        newMultiplier,
                        acc,
                        visited
                );
            }
        }

        visited.remove(recipeId);
    }
}