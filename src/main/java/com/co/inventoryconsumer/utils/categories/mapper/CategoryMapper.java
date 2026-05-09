package com.co.inventoryconsumer.utils.categories.mapper;

import com.co.inventoryconsumer.domain.categories.CategoryDomain;
import com.co.inventoryconsumer.dto.categories.CategoryDTO;
import com.co.inventoryconsumer.dto.categories.CategoryRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(CategoryDomain category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setCns(category.getCns());
        dto.setType(category.getType());
        dto.setSubtype(category.getSubtype());
        dto.setName(category.getName());
        dto.setEntryDate(category.getEntryDate());
        dto.setStatus(category.getStatus());
        return dto;
    }

    public CategoryDomain toDomain(CategoryRequestDTO request) {
        CategoryDomain category = new CategoryDomain();
        category.setType(request.getType());
        category.setSubtype(request.getSubtype());
        category.setName(request.getName());
        return category;
    }
}
