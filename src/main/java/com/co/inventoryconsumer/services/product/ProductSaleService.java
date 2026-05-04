package com.co.inventoryconsumer.services.product;

import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.dto.product.*;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.utils.product.publisher.SaleResponsePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProductSaleService {

    private final ProductRepository repo;
    private final SaleResponsePublisher publisher;

    public ProductSaleService(ProductRepository repo,
                              SaleResponsePublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @Transactional
    public SaleResponseDTO processSale(ProductSaleRequestDTO request) {

        System.out.println("======================================");
        System.out.println("START PROCESS SALE");
        System.out.println("Request received: " + request);
        System.out.println("======================================");

        SaleResponseDTO response = buildBaseResponse(request);

        System.out.println("BASE RESPONSE CREATED: " + response);

        boolean allApproved = true;
        var detailsResponse = new ArrayList<SaleDetailResponseDTO>();

        for (SaleDetailDTO detail : request.getDetails()) {

            System.out.println("PROCESSING DETAIL: " + detail);

            SaleDetailResponseDTO detailResponse = processDetail(detail);

            System.out.println("DETAIL RESULT: " + detailResponse);

            if (!detailResponse.isApproved()) {
                allApproved = false;
            }

            detailsResponse.add(detailResponse);
        }

        response.setDetails(detailsResponse);

        System.out.println("FINAL DETAILS SIZE: " + detailsResponse.size());

        finalizeResponse(response, allApproved);

        System.out.println("FINAL RESPONSE BEFORE PUBLISH: " + response);

        return response;
    }

    public void publishResponse(SaleResponseDTO response) {

        System.out.println("======================================");
        System.out.println("ATTEMPTING TO PUBLISH RESPONSE");
        System.out.println("OBJECT: " + response);
        System.out.println("======================================");

        publisher.publish(response);
    }

    // =========================
    // CORE METHODS
    // =========================

    private SaleResponseDTO buildBaseResponse(ProductSaleRequestDTO request) {

        System.out.println("BUILDING BASE RESPONSE...");

        SaleResponseDTO response = new SaleResponseDTO();

        response.setIdMessage(UUID.randomUUID());
        response.setSellerId(request.getSellerId());
        response.setLocationId(String.valueOf(request.getLocationId()));
        response.setTableId(request.getTableId());

        return response;
    }

    private SaleDetailResponseDTO processDetail(SaleDetailDTO detail) {

        SaleDetailResponseDTO response = new SaleDetailResponseDTO();

        response.setRecipeId(detail.getRecipeId());
        response.setLineDisplayName(detail.getLineDisplayName());

        System.out.println("CHECKING PRODUCT ID: " + detail.getRecipeId());

        /*
        Product product = repo.findById(detail.getRecipeId()).orElse(null);

        if (product == null) {
            System.out.println("PRODUCT NOT FOUND");
            return reject(response, "PRODUCT NOT FOUND");
        }

        if (product.getStock().compareTo(detail.getQuantity()) < 0) {
            System.out.println("INSUFFICIENT STOCK");
            return reject(response, "INSUFFICIENT STOCK");
        }

        product.setStock(product.getStock().subtract(detail.getQuantity()));
        repo.save(product);
        */

        response.setApproved(true);
        response.setReason("APPROVED - STUB MODE");

        return response;
    }

    private void finalizeResponse(SaleResponseDTO response, boolean approved) {

        response.setApproved(approved);

        if (approved) {
            response.setMessage("Venta aprobada correctamente");
        } else {
            response.setMessage("Venta rechazada por validación de inventario");
        }
    }
}