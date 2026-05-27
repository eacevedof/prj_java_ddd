package com.javayog.modules.connection.domain.types;

import com.javayog.modules.shared.domain.enums.ConnectionTypeEnum;
import com.javayog.modules.shared.domain.enums.DatabaseTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * Domain type representing a database connection configuration.
 * This is the domain model, independent of persistence.
 */
@Data
@Builder
public class DatabaseConnectionType {

    private String id;
    private String name;
    private DatabaseTypeEnum databaseType;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String database;
    private ConnectionTypeEnum connectionType;

    // SSH Tunnel fields
    private String sshHost;
    private Integer sshPort;
    private String sshUsername;
    private String sshPassword;
    private String sshPrivateKey;

    // SSL fields
    private Boolean useSsl;
    private String sslCertPath;

    // HTTP Tunnel fields
    private String httpTunnelUrl;

    // UI preferences
    private String colorHex;

    // Connection pool settings
    private Integer maxPoolSize;
    private Integer minIdle;
    private Long connectionTimeout;

    // Metadata
    private Long createdAt;
    private Long updatedAt;
}
