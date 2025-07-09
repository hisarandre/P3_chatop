package com.chatop.backend.exception;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String message) {
        super(message);
    }

    public static InvalidFileTypeException forType(String actualType) {
        return new InvalidFileTypeException(
                String.format("Invalid file type '%s'. Only JPEG, PNG, and WebP are allowed.", actualType)
        );
    }
}