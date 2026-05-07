package com.co.inventoryconsumer.services.product.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.utils.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetProductsByIdsService {

    private final ProductRepository repo;

    public GetProductsByIdsService(ProductRepository repo) {
        this.repo = repo;
    }

    public Map<UUID, Product> run(Set<UUID> ids) {

        List<Product> products = repo.findAllById(ids);

        Set<UUID> foundIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        List<UUID> missing = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missing.isEmpty()) {
            throw new ResourceNotFoundException("Productos no encontrados: " + missing);
        }

        return products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
    }
}