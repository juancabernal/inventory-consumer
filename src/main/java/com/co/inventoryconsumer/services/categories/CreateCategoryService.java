package com.co.inventoryconsumer.services.categories;

import com.co.inventoryconsumer.domain.categories.CategoryDomain;
import com.co.inventoryconsumer.domain.categories.CategoryStatus;
import com.co.inventoryconsumer.dto.categories.CategoryDTO;
import com.co.inventoryconsumer.dto.categories.CategoryRequestDTO;
import com.co.inventoryconsumer.repositories.categories.CategoryRepository;
import com.co.inventoryconsumer.utils.categories.mapper.CategoryMapper;
import com.co.inventoryconsumer.utils.categories.validation.CategoryValidator;
import com.co.inventoryconsumer.utils.exceptions.BusinessException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CreateCategoryService(CategoryRepository repository,
                                 CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CategoryDTO run(CategoryRequestDTO request) {
        CategoryValidator.validateCreateRequest(request);
        validateCategoryNameDoesNotExist(request.getName());
        repository.lockCategoryCnsCounter();

        CategoryDomain category = mapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();

        category.setId(UUID.randomUUID());
        category.setCns(nextCns());
        category.setEntryDate(now);
        category.setStatus(CategoryStatus.ACTIVE);
        category.setCreatedDate(now);
        category.setModifiedDate(now);

        CategoryDomain savedCategory = repository.save(category);
        return mapper.toDto(savedCategory);
    }

    private void validateCategoryNameDoesNotExist(String name) {
        repository.findByName(name)
                .ifPresent(category -> {
                    throw new BusinessException("Ya existe una categoria con este nombre");
                });
    }

    private Long nextCns() {
        return repository.findTopByOrderByCnsDesc()
                .map(CategoryDomain::getCns)
                .map(current -> current + 1)
                .orElse(1L);
    }
}
