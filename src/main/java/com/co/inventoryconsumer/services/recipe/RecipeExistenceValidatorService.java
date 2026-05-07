package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.domain.recipe.RecipeDomain;
import com.co.inventoryconsumer.repositories.recipe.RecipeRepository;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import com.co.inventoryconsumer.utils.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeExistenceValidatorService {

    private static final String RECIPES_NOT_FOUND = "Algunas recetas no existen: %s";
    private static final String RECIPE_IDS_REQUIRED = "La lista de ids de recetas es obligatoria.";

    private final RecipeRepository repo;

    public RecipeExistenceValidatorService(RecipeRepository repo) {
        this.repo = repo;
    }

    public void run(List<UUID> ids) {
        validateIdsRequired(ids);
        validateAllExist(ids);
    }

    private void validateAllExist(List<UUID> ids) {
        var missingIds = findMissingIds(ids);

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(RECIPES_NOT_FOUND, missingIds)
            );
        }
    }

    private List<UUID> findMissingIds(List<UUID> ids) {
        var existingIds = repo.findAllById(ids)
                .stream()
                .map(RecipeDomain::getId)
                .collect(Collectors.toSet());

        return ids.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();
    }

    private void validateIdsRequired(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(
                    RECIPE_IDS_REQUIRED
            );
        }
    }
}