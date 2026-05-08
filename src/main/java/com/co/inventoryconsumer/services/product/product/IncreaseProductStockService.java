package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class IncreaseProductStockService {

    private final ProductRepository repo;

    public IncreaseProductStockService(
            ProductRepository repo
    ) {
        this.repo = repo;
    }

    public void run(
            Map<UUID, Product> products,
            Map<UUID, BigDecimal> quantities
    ) {

        quantities.forEach((productId, quantity) -> {

            Product product =
                    products.get(productId);

            product.setStock(
                    product.getStock().add(quantity)
            );

            repo.save(product);
        });
    }
}