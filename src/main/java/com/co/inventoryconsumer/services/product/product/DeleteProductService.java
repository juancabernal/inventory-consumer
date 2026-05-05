package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.dto.product.product.ProductDeleteMessageDTO;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProductService {

    private final ProductRepository repo;

    public DeleteProductService(ProductRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void run(ProductDeleteMessageDTO request) {
        repo.deleteById(request.getId());
    }
}