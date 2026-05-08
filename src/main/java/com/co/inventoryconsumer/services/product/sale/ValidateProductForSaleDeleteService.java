package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.sale.ProductSaleDeleteRequestDTO;
import com.co.inventoryconsumer.dto.product.sale.SaleDeleteResponseDTO;
import com.co.inventoryconsumer.services.product.product.GetProductsByIdsService;
import com.co.inventoryconsumer.services.product.product.IncreaseProductStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class ValidateProductForSaleDeleteService {

    private final BuildSaleProductsService buildService;
    private final GetProductsByIdsService getProductsService;
    private final IncreaseProductStockService increaseProductStockService;

    public ValidateProductForSaleDeleteService(
            BuildSaleProductsService buildService,
            GetProductsByIdsService getProductsService,
            IncreaseProductStockService increaseProductStockService
    ) {
        this.buildService = buildService;
        this.getProductsService = getProductsService;
        this.increaseProductStockService = increaseProductStockService;
    }

    @Transactional
    public SaleDeleteResponseDTO run(
            ProductSaleDeleteRequestDTO request
    ) {

        Map<UUID, BigDecimal> productsToRestore =
                buildService.run(
                        request.getSale().getDetails()
                );

        if (productsToRestore == null) {

            return SaleDeleteResponseDTO.builder()
                    .saleId(request.getSale().getId())
                    .approved(false)
                    .message("No fue posible procesar las recetas de la venta.")
                    .build();
        }

        Map<UUID, Product> products =
                getProductsService.run(
                        productsToRestore.keySet()
                );

        increaseProductStockService.run(
                products,
                productsToRestore
        );

        return SaleDeleteResponseDTO.builder()
                .saleId(request.getSale().getId())
                .approved(true)
                .message("Venta eliminada correctamente.")
                .build();
    }
}