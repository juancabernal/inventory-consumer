package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class DecreaseProductStockService {

    private final ProductRepository repo;

    public DecreaseProductStockService(ProductRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void run(Map<UUID, Product> products,
                    Map<UUID, BigDecimal> required) {

        for (Map.Entry<UUID, BigDecimal> entry : required.entrySet()) {

            Product product = products.get(entry.getKey());

            BigDecimal newStock = product.getStock()
                    .subtract(entry.getValue());

            product.setStock(newStock);

            repo.save(product);
        }
    }
}