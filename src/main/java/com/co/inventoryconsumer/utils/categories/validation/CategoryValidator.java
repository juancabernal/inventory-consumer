package com.co.inventoryconsumer.utils.categories.validation;

import com.co.inventoryconsumer.domain.categories.CategoryStatus;
import com.co.inventoryconsumer.dto.categories.CategoryRequestDTO;
import com.co.inventoryconsumer.dto.categories.CategoryStatusUpdateDTO;
import com.co.inventoryconsumer.utils.exceptions.ValidationException;
import java.util.UUID;

public class CategoryValidator {

    private CategoryValidator() {
    }

    public static CategoryRequestDTO validateCreateRequest(CategoryRequestDTO request) {
        if (request == null) {
            throw new ValidationException("La solicitud de categoria es obligatoria");
        }

        validateType(request.getType());
        validateSubtype(request.getSubtype());
        validateName(request.getName());

        return request;
    }

    public static CategoryStatus validateStatusUpdateRequest(CategoryStatusUpdateDTO request) {
        if (request == null) {
            throw new ValidationException("La solicitud de estado de categoria es obligatoria");
        }

        return parseRequiredStatus(request.getStatus());
    }

    public static UUID validateId(UUID id) {
        if (id == null) {
            throw new ValidationException("El id de la categoria es obligatorio");
        }
        return id;
    }

    public static String validateType(String type) {
        return requireNonBlank(type, "type");
    }

    public static String validateSubtype(String subtype) {
        return requireNonBlank(subtype, "subtype");
    }

    public static String validateName(String name) {
        return requireNonBlank(name, "name");
    }

    private static CategoryStatus parseRequiredStatus(String status) {
        String normalizedStatus = requireNonBlank(status, "status");

        try {
            return CategoryStatus.valueOf(normalizedStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("El estado de la categoria no es valido");
        }
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("El campo '" + fieldName + "' es obligatorio");
        }
        return value;
    }
}
