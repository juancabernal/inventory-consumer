package com.co.inventoryconsumer.utils.product.mapper;

import com.co.inventoryconsumer.dto.product.sale.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductSaleMapper {

    public SaleResponseDTO toResponse(
            ProductSaleRequestDTO request,
            List<SaleRecipeResponseDTO> recipes
    ) {

        return SaleResponseDTO.builder()
                .idMessage(UUID.randomUUID())
                .locationId(request.getLocationId())
                .sellerId(request.getSellerId())
                .tableId(request.getTableId())
                .recipes(recipes)
                .build();
    }

    public SaleUpdateResponseDTO toUpdateResponse(
            ProductSaleUpdateRequestDTO request,
            List<SaleRecipeResponseDTO> recipes
    ) {

        SaleResponseDTO saleResponse =
                SaleResponseDTO.builder()
                        .idMessage(UUID.randomUUID())
                        .locationId(request.getNewSale().getLocationId())
                        .sellerId(request.getNewSale().getSellerId())
                        .tableId(request.getNewSale().getTableId())
                        .recipes(recipes)
                        .build();

        return SaleUpdateResponseDTO.builder()
                .saleId(request.getOldSale().getId())
                .sale(saleResponse)
                .build();
    }

    public SaleRecipeResponseDTO toRecipeResponse(
            SaleDetailDTO detail,
            boolean approved,
            String message
    ) {

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

    public List<SaleRecipeResponseDTO> toRecipeResponseList(
            List<SaleDetailDTO> details
    ) {

        return details.stream()
                .map(detail ->
                        toRecipeResponse(
                                detail,
                                true,
                                "Receta aprobada"
                        )
                )
                .toList();
    }
}