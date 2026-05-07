package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.sale.ProductSaleRequestDTO;
import com.co.inventoryconsumer.services.product.product.DecreaseProductStockService;
import com.co.inventoryconsumer.services.product.product.GetProductsByIdsService;
import com.co.inventoryconsumer.services.product.product.ValidateProductStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class ValidateAndProcessSaleStockService {

    private final CollectSaleProductsService collectService;
    private final GetProductsByIdsService getProductsService;
    private final ValidateProductStockService validateStockService;
    private final DecreaseProductStockService decreaseStockService;

    public ValidateAndProcessSaleStockService(
            CollectSaleProductsService collectService,
            GetProductsByIdsService getProductsService,
            ValidateProductStockService validateStockService,
            DecreaseProductStockService decreaseStockService
    ) {
        this.collectService = collectService;
        this.getProductsService = getProductsService;
        this.validateStockService = validateStockService;
        this.decreaseStockService = decreaseStockService;
    }

    @Transactional
    public void run(ProductSaleRequestDTO request) {

        Map<UUID, BigDecimal> required =
                collectService.run(request.getDetails());

        Map<UUID, Product> products =
                getProductsService.run(required.keySet());

        validateStockService.run(products, required);
        decreaseStockService.run(products, required);
    }
}