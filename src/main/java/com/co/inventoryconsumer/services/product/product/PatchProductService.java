package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.product.ProductPatchMessageDTO;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.utils.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatchProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public PatchProductService(ProductRepository repo,
                               ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public void run(ProductPatchMessageDTO request) {

        Product product = mapper.toDomainFromPatch(
                request.getId(),
                request.getData()
        );

        repo.save(product);
    }
}