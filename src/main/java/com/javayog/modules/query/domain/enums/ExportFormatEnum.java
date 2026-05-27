package com.javayog.modules.query.domain.enums;

public enum ExportFormatEnum {
    CSV("CSV", ".csv"),
    JSON("JSON", ".json"),
    XML("XML", ".xml"),
    SQL("SQL", ".sql"),
    HTML("HTML", ".html");

    private final String displayName;
    private final String extension;

    ExportFormatEnum(String displayName, String extension) {
        this.displayName = displayName;
        this.extension = extension;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getExtension() {
        return extension;
    }

    public static ExportFormatEnum fromString(String value) {
        for (ExportFormatEnum format : ExportFormatEnum.values()) {
            if (format.name().equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown export format: " + value);
    }
}
