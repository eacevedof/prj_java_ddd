package com.javayog.modules.connection.infrastructure.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import com.javayog.modules.shared.infrastructure.repositories.AbstractJdbcRepository;

import com.javayog.modules.connection.domain.types.DatabaseConnectionType;
import com.javayog.modules.shared.domain.enums.ConnectionTypeEnum;
import com.javayog.modules.shared.domain.enums.DatabaseTypeEnum;

@Repository
public final class ConnectionReaderRepository extends AbstractJdbcRepository {

    public ConnectionReaderRepository(DataSource dataSource) {
        super(dataSource);
    }

    public Optional<DatabaseConnectionType> findById(String id) {
        String sql = """
            -- findById
            SELECT *
            FROM `app_connections`
            WHERE 1
            AND id = ?
            """;

        Optional<Map<String, Object>> result = executeSingleQuery(sql, id);
        return result.map(this::mapToConnection);
    }

    public Optional<DatabaseConnectionType> findByName(String name) {
        String sql = """
            -- findByName
            SELECT *
            FROM `app_connections`
            WHERE 1
            AND name = ?
            """;

        Optional<Map<String, Object>> result = executeSingleQuery(sql, name);
        return result.map(this::mapToConnection);
    }

    public List<DatabaseConnectionType> findAll() {
        String sql = """
            -- findAll
            SELECT *
            FROM `app_connections`
            WHERE 1
            ORDER BY name ASC
            """;

        List<Map<String, Object>> results = executeQuery(sql);
        return results.stream()
            .map(this::mapToConnection)
            .toList();
    }

    private DatabaseConnectionType mapToConnection(Map<String, Object> row) {
        return DatabaseConnectionType.builder()
            .id(mapColumnToString(row, "id"))
            .name(mapColumnToString(row, "name"))
            .databaseType(DatabaseTypeEnum.fromString(mapColumnToString(row, "database_type")))
            .host(mapColumnToString(row, "host"))
            .port(mapColumnToInt(row, "port"))
            .username(mapColumnToString(row, "username"))
            .password(mapColumnToString(row, "password"))
            .database(mapColumnToString(row, "database_name"))
            .connectionType(ConnectionTypeEnum.fromString(mapColumnToString(row, "connection_type")))
            .sshHost(mapColumnToString(row, "ssh_host"))
            .sshPort(mapColumnToInt(row, "ssh_port"))
            .sshUsername(mapColumnToString(row, "ssh_username"))
            .sshPassword(mapColumnToString(row, "ssh_password"))
            .sshPrivateKey(mapColumnToString(row, "ssh_private_key"))
            .useSsl(mapColumnToBoolean(row, "use_ssl"))
            .sslCertPath(mapColumnToString(row, "ssl_cert_path"))
            .httpTunnelUrl(mapColumnToString(row, "http_tunnel_url"))
            .colorHex(mapColumnToString(row, "color_hex"))
            .maxPoolSize(mapColumnToInt(row, "max_pool_size"))
            .minIdle(mapColumnToInt(row, "min_idle"))
            .connectionTimeout(mapColumnToLong(row, "connection_timeout"))
            .createdAt(mapColumnToLong(row, "created_at"))
            .updatedAt(mapColumnToLong(row, "updated_at"))
            .build();
    }

    private Boolean mapColumnToBoolean(Map<String, Object> row, String columnName) {
        Object value = row.get(columnName);
        if (value == null) return false;
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return Boolean.parseBoolean(value.toString());
    }

    private Long mapColumnToLong(Map<String, Object> row, String columnName) {
        Object value = row.get(columnName);
        if (value == null) return null;
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }
}
