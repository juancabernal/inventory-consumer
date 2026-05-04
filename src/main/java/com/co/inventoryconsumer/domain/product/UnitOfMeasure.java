package com.co.inventoryconsumer.domain.product;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UnitOfMeasure {
    KG,
    GR,
    LI,
    UNI;

    @JsonCreator
    public static UnitOfMeasure from(String value) {
        return UnitOfMeasure.valueOf(value.toUpperCase());
    }
}