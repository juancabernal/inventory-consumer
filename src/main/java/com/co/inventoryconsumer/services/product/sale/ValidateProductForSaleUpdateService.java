package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.sale.*;
import com.co.inventoryconsumer.services.product.product.DecreaseProductStockService;
import com.co.inventoryconsumer.services.product.product.GetProductsByIdsService;
import com.co.inventoryconsumer.services.product.product.IncreaseProductStockService;
import com.co.inventoryconsumer.utils.product.mapper.ProductSaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ValidateProductForSaleUpdateService {

    private final BuildSaleProductsService buildService;
    private final GetProductsByIdsService getProductsService;
    private final DecreaseProductStockService decreaseStockService;
    private final IncreaseProductStockService increaseProductStockService;
    private final ExpandRecipeProductsService expandService;
    private final ProductSaleMapper mapper;

    public ValidateProductForSaleUpdateService(
            BuildSaleProductsService buildService,
            GetProductsByIdsService getProductsService,
            DecreaseProductStockService decreaseStockService,
            IncreaseProductStockService increaseProductStockService,
            ExpandRecipeProductsService expandService,
            ProductSaleMapper mapper
    ) {
        this.buildService = buildService;
        this.getProductsService = getProductsService;
        this.decreaseStockService = decreaseStockService;
        this.increaseProductStockService = increaseProductStockService;
        this.expandService = expandService;
        this.mapper = mapper;
    }

    @Transactional
    public SaleUpdateResponseDTO run(
            ProductSaleUpdateRequestDTO request
    ) {

        List<SaleRecipeResponseDTO> responses = new ArrayList<>();

        boolean allApproved = true;

        for (SaleDetailDTO detail : request.getNewSale().getDetails()) {

            Map<UUID, BigDecimal> recipeProducts =
                    expandService.run(
                            detail.getRecipeId(),
                            detail.getQuantity()
                    );

            if (recipeProducts == null) {

                responses.add(
                        mapper.toRecipeResponse(
                                detail,
                                false,
                                String.format(
                                        "La receta con id %s no existe.",
                                        detail.getRecipeId()
                                )
                        )
                );

                allApproved = false;

                continue;
            }

            Map<UUID, Product> products =
                    getProductsService.run(
                            recipeProducts.keySet()
                    );

            boolean stockValid = true;

            for (Map.Entry<UUID, BigDecimal> entry : recipeProducts.entrySet()) {

                Product product =
                        products.get(entry.getKey());

                if (product == null) {

                    responses.add(
                            mapper.toRecipeResponse(
                                    detail,
                                    false,
                                    "Uno de los productos no existe."
                            )
                    );

                    stockValid = false;
                    allApproved = false;

                    break;
                }

                if (product.getStock().compareTo(entry.getValue()) < 0) {

                    responses.add(
                            mapper.toRecipeResponse(
                                    detail,
                                    false,
                                    "Stock insuficiente para la receta."
                            )
                    );

                    stockValid = false;
                    allApproved = false;

                    break;
                }
            }

            if (stockValid) {

                responses.add(
                        mapper.toRecipeResponse(
                                detail,
                                true,
                                "Receta aprobada"
                        )
                );
            }
        }

        if (allApproved) {

            Map<UUID, BigDecimal> oldProducts =
                    buildService.run(
                            request.getOldSale().getDetails()
                    );

            Map<UUID, BigDecimal> newProducts =
                    buildService.run(
                            request.getNewSale().getDetails()
                    );

            Map<UUID, BigDecimal> difference =
                    calculateDifference(
                            oldProducts,
                            newProducts
                    );

            Map<UUID, Product> products =
                    getProductsService.run(
                            difference.keySet()
                    );

            processStockDifference(
                    products,
                    difference
            );
        }

        SaleResponseDTO response =
                mapper.toResponse(
                        request.getNewSale(),
                        responses
                );

        return SaleUpdateResponseDTO.builder()
                .saleId(request.getOldSale().getId())
                .sale(response)
                .build();
    }

    private Map<UUID, BigDecimal> calculateDifference(
            Map<UUID, BigDecimal> oldProducts,
            Map<UUID, BigDecimal> newProducts
    ) {

        Map<UUID, BigDecimal> result = new HashMap<>();

        Set<UUID> allKeys = new HashSet<>();

        allKeys.addAll(oldProducts.keySet());
        allKeys.addAll(newProducts.keySet());

        for (UUID productId : allKeys) {

            BigDecimal oldQty =
                    oldProducts.getOrDefault(
                            productId,
                            BigDecimal.ZERO
                    );

            BigDecimal newQty =
                    newProducts.getOrDefault(
                            productId,
                            BigDecimal.ZERO
                    );

            result.put(
                    productId,
                    newQty.subtract(oldQty)
            );
        }

        return result;
    }

    private void processStockDifference(
            Map<UUID, Product> products,
            Map<UUID, BigDecimal> difference
    ) {

        Map<UUID, BigDecimal> toDecrease =
                new HashMap<>();

        Map<UUID, BigDecimal> toIncrease =
                new HashMap<>();

        difference.forEach((productId, quantity) -> {

            if (quantity.compareTo(BigDecimal.ZERO) > 0) {

                toDecrease.put(
                        productId,
                        quantity
                );

            } else if (quantity.compareTo(BigDecimal.ZERO) < 0) {

                toIncrease.put(
                        productId,
                        quantity.abs()
                );
            }
        });

        if (!toDecrease.isEmpty()) {

            decreaseStockService.run(
                    products,
                    toDecrease
            );
        }

        if (!toIncrease.isEmpty()) {

            increaseProductStockService.run(
                    products,
                    toIncrease
            );
        }
    }
}