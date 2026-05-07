package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.domain.recipe.RecipeDomain;
import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import com.co.inventoryconsumer.dto.recipe.RecipeSubRecipeRequest;
import com.co.inventoryconsumer.repositories.recipe.RecipeRepository;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CreateRecipeService {

    public static final String RECIPE_WITH_NAME_EXISTS = "La receta con el nombre %s ya existe.";

    private final RecipeRepository repo;
    private final RecipeMapper mapper;
    private final GenerateRecipeIdService idService;
    private final RecipeExistenceValidatorService existenceValidator;
    private final CalculateRecipeCostService costService;
    private final CalculateRecipeSellingPriceService sellingPriceService;

    public CreateRecipeService(
            RecipeRepository repo,
            RecipeMapper mapper,
            GenerateRecipeIdService idService,
            RecipeExistenceValidatorService existenceValidator,
            CalculateRecipeCostService costService,
            CalculateRecipeSellingPriceService sellingPriceService
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.idService = idService;
        this.existenceValidator = existenceValidator;
        this.costService = costService;
        this.sellingPriceService = sellingPriceService;
    }

    @Transactional
    public void run(RecipeRequest request) {

        validatePreviousExistence(request.getName());
        validateSubRecipesIfPresent(request);

        UUID id = idService.run();

        RecipeDomain recipe = mapper.toNewDomain(request, id);

        if (recipe.getSubRecipes() == null) {
            recipe.setSubRecipes(List.of());
        }

        BigDecimal baseCost = costService.run(request);

        BigDecimal sellingPrice = sellingPriceService.run(
                baseCost,
                request.getProfitMargin()
        );

        recipe.setBaseCost(baseCost);
        recipe.setSellingPrice(sellingPrice);

        repo.save(recipe);
    }

    private void validatePreviousExistence(String name) {

        if (repo.existsByName(name)) {

            throw new BusinessException(
                    String.format(RECIPE_WITH_NAME_EXISTS, name)
            );
        }
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
}