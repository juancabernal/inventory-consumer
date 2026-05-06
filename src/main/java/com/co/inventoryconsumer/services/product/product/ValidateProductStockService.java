package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class ValidateProductStockService {

    public void run(Map<UUID, Product> products,
                    Map<UUID, BigDecimal> required) {

        for (Map.Entry<UUID, BigDecimal> entry : required.entrySet()) {

            UUID productId = entry.getKey();
            BigDecimal requiredQty = entry.getValue();

            Product product = products.get(productId);

            if (product.getStock() == null) {
                throw new BusinessException("Producto sin stock definido: " + product.getName());
            }

            if (product.getStock().compareTo(requiredQty) < 0) {
                throw new BusinessException(
                        "Stock insuficiente para producto " + product.getName() +
                                ". Requerido: " + requiredQty +
                                ", Disponible: " + product.getStock()
                );
            }
        }
    }
}