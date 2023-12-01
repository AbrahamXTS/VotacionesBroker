package com.bichotitas.votaciones.broker.exception;

public class BrokerIsDeadException extends RuntimeException {
    public BrokerIsDeadException(String message) {
        super(message);
    }
}
