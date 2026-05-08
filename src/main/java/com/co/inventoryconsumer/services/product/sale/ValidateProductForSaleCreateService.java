package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.sale.ProductSaleRequestDTO;
import com.co.inventoryconsumer.dto.product.sale.SaleRecipeResponseDTO;
import com.co.inventoryconsumer.dto.product.sale.SaleResponseDTO;
import com.co.inventoryconsumer.services.product.product.DecreaseProductStockService;
import com.co.inventoryconsumer.services.product.product.GetProductsByIdsService;
import com.co.inventoryconsumer.utils.product.mapper.ProductSaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ValidateProductForSaleCreateService {

    private final CollectSaleProductsService collectService;
    private final GetProductsByIdsService getProductsService;
    private final DecreaseProductStockService decreaseStockService;
    private final ExpandRecipeProductsService expandService;
    private final ProductSaleMapper mapper;

    public ValidateProductForSaleCreateService(
            CollectSaleProductsService collectService,
            GetProductsByIdsService getProductsService,
            DecreaseProductStockService decreaseStockService,
            ExpandRecipeProductsService expandService,
            ProductSaleMapper mapper
    ) {
        this.collectService = collectService;
        this.getProductsService = getProductsService;
        this.decreaseStockService = decreaseStockService;
        this.expandService = expandService;
        this.mapper = mapper;
    }

    @Transactional
    public SaleResponseDTO run(ProductSaleRequestDTO request) {

        List<SaleRecipeResponseDTO> responses = new ArrayList<>();

        boolean allApproved = true;

        for (var detail : request.getDetails()) {

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

            Map<UUID, BigDecimal> requiredProducts =
                    collectService.run(request.getDetails());

            if (requiredProducts != null) {

                Map<UUID, Product> products =
                        getProductsService.run(
                                requiredProducts.keySet()
                        );

                decreaseStockService.run(
                        products,
                        requiredProducts
                );
            }
        }

        return mapper.toResponse(
                request,
                responses
        );
    }
}