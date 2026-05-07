package com.co.inventoryconsumer.services.recipe;

import com.co.inventoryconsumer.dto.recipe.RecipeRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculateProductsCostService {

    public BigDecimal run(RecipeRequest request) {

        return request.getProducts()
                .stream()
                .map(p ->
                        p.getPrice().multiply(
                                BigDecimal.valueOf(p.getQuantity())
                        )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}