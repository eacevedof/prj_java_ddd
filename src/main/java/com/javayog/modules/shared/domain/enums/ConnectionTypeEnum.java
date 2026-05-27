package com.javayog.modules.shared.domain.enums;

/**
 * Connection methods
 */
public enum ConnectionTypeEnum {
    DIRECT("Direct"),
    SSH_TUNNEL("SSH Tunnel"),
    HTTP_TUNNEL("HTTP Tunnel"),
    SSL("SSL");

    private final String displayName;

    ConnectionTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
