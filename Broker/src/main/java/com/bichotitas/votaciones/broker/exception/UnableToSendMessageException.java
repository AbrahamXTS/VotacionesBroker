package com.bichotitas.votaciones.broker.exception;

public class UnableToSendMessageException extends RuntimeException {
    public UnableToSendMessageException(String message) {
        super(message);
    }
}
