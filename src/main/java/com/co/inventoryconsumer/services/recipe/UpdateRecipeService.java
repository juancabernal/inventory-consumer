package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.domain.recipe.RecipeDomain;
import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import com.co.inventoryconsumer.dto.recipe.RecipeSubRecipeRequest;
import com.co.inventoryconsumer.repositories.recipe.RecipeRepository;
import com.co.inventoryconsumer.utils.exceptions.ResourceNotFoundException;
import com.co.inventoryconsumer.utils.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UpdateRecipeService {

    public static final String RECIPE_NOT_FOUND = "La receta con el nombre %s no fue encontrada.";

    private final RecipeRepository repo;
    private final RecipeMapper mapper;
    private final RecipeExistenceValidatorService existenceValidator;
    private final CalculateRecipeCostService costService;
    private final CalculateRecipeSellingPriceService sellingPriceService;
    private final RecalculateDependentRecipesCostService recalculateDependentRecipesCostService;

    public UpdateRecipeService(
            RecipeRepository repo,
            RecipeMapper mapper,
            RecipeExistenceValidatorService existenceValidator,
            CalculateRecipeCostService costService,
            CalculateRecipeSellingPriceService sellingPriceService, RecalculateDependentRecipesCostService recalculateDependentRecipesCostService
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.existenceValidator = existenceValidator;
        this.costService = costService;
        this.sellingPriceService = sellingPriceService;
        this.recalculateDependentRecipesCostService = recalculateDependentRecipesCostService;
    }

    @Transactional
    public void run(RecipeRequest request) {

        validateSubRecipesIfPresent(request);

        RecipeDomain existingRecipe = getExistingRecipe(request.getName());

        mapper.toUpdatedDomain(request, existingRecipe);

        if (existingRecipe.getSubRecipes() == null) {
            existingRecipe.setSubRecipes(new ArrayList<>());
        }

        BigDecimal baseCost = costService.run(request);

        BigDecimal sellingPrice = sellingPriceService.run(
                baseCost,
                request.getProfitMargin()
        );

        existingRecipe.setBaseCost(baseCost);
        existingRecipe.setSellingPrice(sellingPrice);

        repo.save(existingRecipe);

        recalculateDependentRecipesCostService.run(
                existingRecipe.getId()
        );
    }

    private void validateSubRecipesIfPresent(RecipeRequest request) {

        if (request.getSubRecipes() == null || request.getSubRecipes().isEmpty()) {
            return;
        }

        List<UUID> ids = request.getSubRecipes()
                .stream()
                .map(RecipeSubRecipeRequest::getSubRecipeId)
                .toList();

        existenceValidator.run(ids);
    }

    private RecipeDomain getExistingRecipe(String name) {

        return repo.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(RECIPE_NOT_FOUND, name)
                ));
    }
}