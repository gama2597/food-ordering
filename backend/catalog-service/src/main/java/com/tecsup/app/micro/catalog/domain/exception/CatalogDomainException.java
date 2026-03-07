package com.tecsup.app.micro.catalog.domain.exception;

public class CatalogDomainException extends RuntimeException {

    public CatalogDomainException(String message) {
        super(message);
    }

    public CatalogDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}