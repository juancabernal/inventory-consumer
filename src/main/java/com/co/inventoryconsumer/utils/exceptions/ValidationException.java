package com.co.inventoryconsumer.utils.exceptions;

public class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}