package com.co.inventoryconsumer.utils.exceptions;

import java.time.LocalDateTime;

/**
 * Clase base para todas las excepciones de la aplicación.
 * Incluye un código de error y una marca de tiempo.
 */
public abstract class ApplicationException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime timestamp;

    protected ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
