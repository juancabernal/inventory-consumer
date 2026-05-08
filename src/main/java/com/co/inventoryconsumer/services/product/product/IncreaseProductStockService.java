package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.product.StockUpdateDTO;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class IncreaseProductStockService {

    private final ProductRepository repo;

    public IncreaseProductStockService(ProductRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void run(StockUpdateDTO dto) {
        UUID productId = UUID.fromString(dto.getProductId());

        Product product = repo.findById(productId)
                .orElseThrow(() -> new BusinessException(
                        "Producto no encontrado con id: " + productId));

        product.setStock(product.getStock().add(dto.getQuantity()));
        repo.save(product);
    }
}