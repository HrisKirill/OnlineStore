package com.khrystoforov.onlinestore.exception;

public class ProductQuantityException extends RuntimeException {
    public ProductQuantityException(String message) {
        super(message);
    }
}
