package com.co.inventoryconsumer.services.product.sale;

import com.co.inventoryconsumer.dto.product.sale.*;
import com.co.inventoryconsumer.utils.product.mapper.ProductSaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ValidateProductForSaleCreateService {

    private final ProductSaleMapper mapper;

    public ValidateProductForSaleCreateService(ProductSaleMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public SaleResponseDTO run(ProductSaleRequestDTO request) {

        List<SaleRecipeResponseDTO> recipes =
                mapper.toRecipeResponseList(request.getDetails());

        return mapper.toResponse(request, recipes);
    }
}