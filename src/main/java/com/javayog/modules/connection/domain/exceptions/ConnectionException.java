package com.javayog.modules.connection.domain.exceptions;

import com.javayog.modules.shared.domain.exceptions.DomainException;

public final class ConnectionException extends DomainException {

    public ConnectionException(String message, int statusCode, String errorCode) {
        super(message, statusCode, errorCode);
    }

    public ConnectionException(String message, int statusCode, String errorCode, Throwable cause) {
        super(message, statusCode, errorCode, cause);
    }

    // Factory methods
    public static ConnectionException badRequestCustom(String message) {
        return new ConnectionException(message, 400, "CONNECTION_BAD_REQUEST");
    }

    public static ConnectionException notFound(String connectionName) {
        return new ConnectionException(
            String.format("Connection '%s' not found", connectionName),
            404,
            "CONNECTION_NOT_FOUND"
        );
    }

    public static ConnectionException alreadyExists(String connectionName) {
        return new ConnectionException(
            String.format("Connection '%s' already exists", connectionName),
            409,
            "CONNECTION_ALREADY_EXISTS"
        );
    }

    public static ConnectionException testFailed(String message, Throwable cause) {
        return new ConnectionException(
            "Connection test failed: " + message,
            500,
            "CONNECTION_TEST_FAILED",
            cause
        );
    }
}
