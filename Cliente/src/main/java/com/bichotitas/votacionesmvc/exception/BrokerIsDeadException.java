package com.bichotitas.votacionesmvc.exception;

public class BrokerIsDeadException extends RuntimeException {
    public BrokerIsDeadException(String message) {
        super(message);
    }
}
