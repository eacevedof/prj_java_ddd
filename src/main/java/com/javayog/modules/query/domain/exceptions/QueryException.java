package com.javayog.modules.query.domain.exceptions;

import com.javayog.modules.shared.domain.exceptions.DomainException;

public final class QueryException extends DomainException {

    public QueryException(String message, int statusCode, String errorCode) {
        super(message, statusCode, errorCode);
    }

    public QueryException(String message, int statusCode, String errorCode, Throwable cause) {
        super(message, statusCode, errorCode, cause);
    }

    // Factory methods
    public static QueryException executionFailed(String message, Throwable cause) {
        return new QueryException(
            "Query execution failed: " + message,
            500,
            "QUERY_EXECUTION_FAILED",
            cause
        );
    }

    public static QueryException invalidSyntax(String message) {
        return new QueryException(
            "Invalid SQL syntax: " + message,
            400,
            "QUERY_INVALID_SYNTAX"
        );
    }

    public static QueryException noConnectionSelected() {
        return new QueryException(
            "No database connection selected",
            400,
            "QUERY_NO_CONNECTION"
        );
    }

    public static QueryException updateFailed(String message, Throwable cause) {
        return new QueryException(
            "Row update failed: " + message,
            500,
            "QUERY_UPDATE_FAILED",
            cause
        );
    }

    public static QueryException deleteFailed(String message, Throwable cause) {
        return new QueryException(
            "Row delete failed: " + message,
            500,
            "QUERY_DELETE_FAILED",
            cause
        );
    }

    public static QueryException exportFailed(String message, Throwable cause) {
        return new QueryException(
            "Export failed: " + message,
            500,
            "QUERY_EXPORT_FAILED",
            cause
        );
    }
}
