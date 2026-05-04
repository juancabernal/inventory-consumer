package com.co.inventoryconsumer.services.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.ProductRequestDTO;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.utils.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public CreateProductService(ProductRepository repo,
                                ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public void run(ProductRequestDTO request) {

        Product product = mapper.toDomain(request);

        repo.save(product);
    }
}