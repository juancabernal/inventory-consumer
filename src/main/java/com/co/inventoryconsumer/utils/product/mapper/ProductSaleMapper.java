package com.co.inventoryconsumer.utils.product.mapper;

import com.co.inventoryconsumer.dto.product.sale.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductSaleMapper {

    public SaleResponseDTO toResponse(ProductSaleRequestDTO request,
                                      List<SaleRecipeResponseDTO> recipes) {

        return SaleResponseDTO.builder()
                .idMessage(UUID.randomUUID())
                .locationId(request.getLocationId())
                .sellerId(request.getSellerId())
                .tableId(request.getTableId())
                .recipes(recipes)
                .build();
    }

    public SaleRecipeResponseDTO toRecipeResponse(SaleDetailDTO detail,
                                                  boolean approved,
                                                  String message) {

        return SaleRecipeResponseDTO.builder()
                .recipeId(detail.getRecipeId())
                .lineDisplayName(detail.getLineDisplayName())
                .recipeLineComment(detail.getRecipeLineComment())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getSubtotal())
                .approved(approved)
                .message(message)
                .build();
    }

    public List<SaleRecipeResponseDTO> toRecipeResponseList(List<SaleDetailDTO> details) {
        return details.stream()
                .map(d -> toRecipeResponse(
                        d,
                        true,
                        "Receta aprobada"
                ))
                .toList();
    }
}