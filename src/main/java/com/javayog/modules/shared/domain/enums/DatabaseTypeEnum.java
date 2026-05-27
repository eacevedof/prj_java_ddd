package com.javayog.modules.shared.domain.enums;

/**
 * Supported database types
 */
public enum DatabaseTypeEnum {
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL");

    private final String displayName;

    DatabaseTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DatabaseTypeEnum fromString(String type) {
        return DatabaseTypeEnum.valueOf(type.toUpperCase());
    }
}
