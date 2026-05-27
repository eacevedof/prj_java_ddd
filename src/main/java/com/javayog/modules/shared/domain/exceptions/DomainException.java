package com.javayog.modules.shared.domain.exceptions;

import lombok.Getter;

/**
 * Base exception for all domain exceptions.
 * All module-specific exceptions should extend this.
 */
@Getter
public class DomainException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;

    public DomainException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public DomainException(String message, int statusCode, String errorCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    // Factory methods for common HTTP status codes
    public static DomainException badRequest(String message) {
        return new DomainException(message, 400, "BAD_REQUEST");
    }

    public static DomainException unauthorized(String message) {
        return new DomainException(message, 401, "UNAUTHORIZED");
    }

    public static DomainException forbidden(String message) {
        return new DomainException(message, 403, "FORBIDDEN");
    }

    public static DomainException notFound(String message) {
        return new DomainException(message, 404, "NOT_FOUND");
    }

    public static DomainException internalError(String message) {
        return new DomainException(message, 500, "INTERNAL_ERROR");
    }

    public static DomainException internalError(String message, Throwable cause) {
        return new DomainException(message, 500, "INTERNAL_ERROR", cause);
    }
}
