package com.co.inventoryconsumer.utils.exceptions;

public class BusinessException extends ApplicationException {
    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}

