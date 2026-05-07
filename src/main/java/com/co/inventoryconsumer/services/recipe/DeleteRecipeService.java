package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.domain.recipe.RecipeDomain;
import com.co.inventoryconsumer.repositories.recipe.RecipeRepository;
import com.co.inventoryconsumer.utils.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteRecipeService {

    public static final String RECIPE_NOT_FOUND = "La receta con el nombre %s no fue encontrada.";

    private final RecipeRepository repo;

    public DeleteRecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void run(String name) {
        var recipe = this.getActiveRecipe(name);
        recipe.deactivate();
        repo.save(recipe);
    }

    private RecipeDomain getActiveRecipe(String name) {
        return repo.findByNameAndActiveTrue(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(RECIPE_NOT_FOUND, name)
                ));
    }
}