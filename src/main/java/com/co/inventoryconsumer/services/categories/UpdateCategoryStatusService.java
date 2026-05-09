package com.co.inventoryconsumer.services.categories;

import com.co.inventoryconsumer.domain.categories.CategoryDomain;
import com.co.inventoryconsumer.domain.categories.CategoryStatus;
import com.co.inventoryconsumer.dto.categories.CategoryDTO;
import com.co.inventoryconsumer.dto.categories.CategoryStatusUpdateDTO;
import com.co.inventoryconsumer.repositories.categories.CategoryRepository;
import com.co.inventoryconsumer.utils.categories.mapper.CategoryMapper;
import com.co.inventoryconsumer.utils.categories.validation.CategoryValidator;
import com.co.inventoryconsumer.utils.exceptions.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateCategoryStatusService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public UpdateCategoryStatusService(CategoryRepository repository,
                                       CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CategoryDTO run(UUID id, CategoryStatusUpdateDTO request) {
        CategoryValidator.validateId(id);
        CategoryStatus status = CategoryValidator.validateStatusUpdateRequest(request);

        CategoryDomain category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        category.setStatus(status);
        category.setModifiedDate(LocalDateTime.now());

        CategoryDomain updatedCategory = repository.save(category);
        return mapper.toDto(updatedCategory);
    }
}
