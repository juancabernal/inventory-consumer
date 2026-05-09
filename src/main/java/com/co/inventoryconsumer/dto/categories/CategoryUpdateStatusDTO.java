package com.co.inventoryconsumer.dto.categories;

import java.util.UUID;

public class CategoryUpdateStatusDTO {

    private UUID id;
    private CategoryStatusUpdateDTO data;

    public CategoryUpdateStatusDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CategoryStatusUpdateDTO getData() {
        return data;
    }

    public void setData(CategoryStatusUpdateDTO data) {
        this.data = data;
    }
}
