package com.co.inventoryconsumer.dto.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequestDTO {

    @NotBlank(message = "El tipo de la categoria es obligatorio")
    private String type;

    @NotBlank(message = "El subtipo de la categoria es obligatorio")
    private String subtype;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre de la categoria debe tener entre 2 y 100 caracteres")
    private String name;

    public CategoryRequestDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
