package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.dto.recipe.RecipeResponse;
import com.co.inventoryconsumer.repositories.recipe.RecipeRepository;
import com.co.inventoryconsumer.utils.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetRecipeByIdService {

    private final RecipeRepository repo;
    private final RecipeMapper mapper;

    public GetRecipeByIdService(RecipeRepository repo, RecipeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public RecipeResponse run(UUID id) {

        if (id == null) {
            return null;
        }

        return repo.findById(id)
                .map(mapper::toResponse)
                .orElse(null);
    }
}