package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.product.sale.*;
import com.co.inventoryconsumer.utils.product.mapper.ProductSaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ValidateProductForSaleCreateService {

    private final ValidateAndProcessSaleStockService stockService;
    private final ProductSaleMapper mapper;

    public ValidateProductForSaleCreateService(
            ValidateAndProcessSaleStockService stockService,
            ProductSaleMapper mapper
    ) {
        this.stockService = stockService;
        this.mapper = mapper;
    }

    @Transactional
    public SaleResponseDTO run(ProductSaleRequestDTO request) {

        stockService.run(request);

        var recipes =
                mapper.toRecipeResponseList(request.getDetails());

        return mapper.toResponse(request, recipes);
    }
}