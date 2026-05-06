package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.product.ProductUpdateDTO;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.utils.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public UpdateProductService(ProductRepository repo,
                                ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public void run(ProductUpdateDTO request) {

        Product product = mapper.toDomain(request.getData());
        product.setId(request.getId());

        repo.save(product);
    }
}