package com.co.inventoryconsumer.utils.recipe.mapper;

import com.co.inventoryconsumer.domain.recipe.RecipeDomain;
import com.co.inventoryconsumer.domain.recipe.RecipeProduct;
import com.co.inventoryconsumer.domain.recipe.RecipeSubRecipe;
import com.co.inventoryconsumer.dto.recipe.RecipeProductRequest;
import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import com.co.inventoryconsumer.dto.recipe.RecipeResponse;
import com.co.inventoryconsumer.dto.recipe.RecipeSubRecipeRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class RecipeMapper {

    public RecipeResponse toResponse(RecipeDomain recipe) {

        if (recipe == null) {
            return null;
        }

        RecipeResponse response = new RecipeResponse();

        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setCategoryId(recipe.getCategoryId());
        response.setLocationId(recipe.getLocationId());
        response.setProducts(mapProductsToResponse(recipe.getProducts()));
        response.setSubRecipes(mapSubRecipesToResponse(recipe.getSubRecipes()));
        response.setBaseCost(recipe.getBaseCost());
        response.setProfitMargin(recipe.getProfitMargin());
        response.setSellingPrice(recipe.getSellingPrice());
        response.setVisibleInMenu(recipe.getVisibleInMenu());
        response.setActive(recipe.getActive());
        response.setCreatedAt(recipe.getCreatedAt());
        response.setUpdatedAt(recipe.getUpdatedAt());

        return response;
    }

    public RecipeDomain toNewDomain(RecipeRequest request, UUID id) {

        RecipeDomain recipe = new RecipeDomain();

        recipe.setId(id);
        recipe.setName(request.getName());
        recipe.setCategoryId(request.getCategoryId());
        recipe.setLocationId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        recipe.setProducts(mapProducts(request.getProducts()));
        recipe.setSubRecipes(mapSubRecipes(request.getSubRecipes()));
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        recipe.setVisibleInMenu(request.getVisibleInMenu());
        recipe.setActive(request.getActive());
        recipe.setProfitMargin(request.getProfitMargin());

        return recipe;
    }

    public void toUpdatedDomain(RecipeRequest request, RecipeDomain existing) {

        existing.setName(request.getName());
        existing.setCategoryId(request.getCategoryId());
        existing.setProducts(mapProducts(request.getProducts()));
        existing.setSubRecipes(mapSubRecipes(request.getSubRecipes()));
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setVisibleInMenu(request.getVisibleInMenu());
        existing.setActive(request.getActive());
        existing.setProfitMargin(request.getProfitMargin());
    }

    private List<RecipeProduct> mapProducts(List<RecipeProductRequest> products) {

        if (products == null) {
            return List.of();
        }

        return products.stream().map(p -> {
            RecipeProduct product = new RecipeProduct();
            product.setProductId(p.getProductId());
            product.setQuantity(p.getQuantity());
            product.setPrice(p.getPrice());
            return product;
        }).toList();
    }

    private List<RecipeSubRecipe> mapSubRecipes(List<RecipeSubRecipeRequest> subs) {

        if (subs == null) {
            return List.of();
        }

        return subs.stream().map(s -> {
            RecipeSubRecipe sub = new RecipeSubRecipe();
            sub.setSubRecipeId(s.getSubRecipeId());
            sub.setQuantity(s.getQuantity());
            return sub;
        }).toList();
    }

    private List<RecipeProductRequest> mapProductsToResponse(List<RecipeProduct> products) {

        if (products == null) {
            return List.of();
        }

        return products.stream().map(p -> {
            RecipeProductRequest dto = new RecipeProductRequest();
            dto.setProductId(p.getProductId());
            dto.setQuantity(p.getQuantity());
            dto.setPrice(p.getPrice());
            return dto;
        }).toList();
    }

    private List<RecipeSubRecipeRequest> mapSubRecipesToResponse(List<RecipeSubRecipe> subs) {

        if (subs == null) {
            return List.of();
        }

        return subs.stream().map(s -> {
            RecipeSubRecipeRequest dto = new RecipeSubRecipeRequest();
            dto.setSubRecipeId(s.getSubRecipeId());
            dto.setQuantity(s.getQuantity());
            return dto;
        }).toList();
    }
}