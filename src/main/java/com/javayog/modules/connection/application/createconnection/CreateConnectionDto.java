package com.javayog.modules.connection.application.createconnection;

import com.javayog.modules.shared.domain.enums.ConnectionTypeEnum;
import com.javayog.modules.shared.domain.enums.DatabaseTypeEnum;

public record CreateConnectionDto(
    String name,
    DatabaseTypeEnum databaseType,
    String host,
    Integer port,
    String username,
    String password,
    String database,
    ConnectionTypeEnum connectionType,
    String sshHost,
    Integer sshPort,
    String sshUsername,
    String sshPassword,
    String sshPrivateKey,
    Boolean useSsl,
    String sslCertPath,
    String httpTunnelUrl,
    String colorHex
) {

    public static CreateConnectionDto fromPrimitives(
        String name,
        String databaseType,
        String host,
        Integer port,
        String username,
        String password,
        String database
    ) {
        return new CreateConnectionDto(
            name,
            DatabaseTypeEnum.fromString(databaseType),
            host,
            port,
            username,
            password,
            database,
            ConnectionTypeEnum.DIRECT,
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            null
        );
    }
}
