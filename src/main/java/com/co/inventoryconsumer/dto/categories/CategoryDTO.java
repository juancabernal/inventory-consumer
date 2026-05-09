package com.co.inventoryconsumer.dto.categories;

import com.co.inventoryconsumer.domain.categories.CategoryStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class CategoryDTO {

    private UUID id;
    private Long cns;
    private String type;
    private String subtype;
    private String name;
    private LocalDateTime entryDate;
    private CategoryStatus status;

    public CategoryDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCns() {
        return cns;
    }

    public void setCns(Long cns) {
        this.cns = cns;
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

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }
}
