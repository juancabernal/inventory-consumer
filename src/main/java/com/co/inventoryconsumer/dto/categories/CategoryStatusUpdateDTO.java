package com.co.inventoryconsumer.dto.categories;

import jakarta.validation.constraints.NotBlank;

public class CategoryStatusUpdateDTO {

    @NotBlank(message = "El estado de la categoria es obligatorio")
    private String status;

    public CategoryStatusUpdateDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
