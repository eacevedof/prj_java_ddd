package com.javayog.modules.connection.infrastructure.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.javayog.modules.shared.infrastructure.repositories.AbstractJdbcRepository;

import com.javayog.modules.connection.domain.types.DatabaseConnectionType;

import javax.sql.DataSource;

@Repository
public final class ConnectionWriterRepository extends AbstractJdbcRepository {

    public ConnectionWriterRepository(DataSource dataSource) {
        super(dataSource);
    }

    public boolean existsByName(String name) {
        String sql = """
            -- existsByName
            SELECT COUNT(*)
            FROM `app_connections`
            WHERE 1
            AND name = ?
            """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public void save(DatabaseConnectionType connection) {
        String sql = """
            -- save
            INSERT INTO `app_connections` (
                id, name, database_type, host, port, username, password,
                database_name, connection_type, ssh_host, ssh_port, ssh_username,
                ssh_password, ssh_private_key, use_ssl, ssl_cert_path,
                http_tunnel_url, color_hex, max_pool_size, min_idle,
                connection_timeout, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        executeUpdate(
            sql,
            connection.getId(),
            connection.getName(),
            connection.getDatabaseType().name(),
            connection.getHost(),
            connection.getPort(),
            connection.getUsername(),
            connection.getPassword(),
            connection.getDatabase(),
            connection.getConnectionType().name(),
            connection.getSshHost(),
            connection.getSshPort(),
            connection.getSshUsername(),
            connection.getSshPassword(),
            connection.getSshPrivateKey(),
            connection.getUseSsl(),
            connection.getSslCertPath(),
            connection.getHttpTunnelUrl(),
            connection.getColorHex(),
            connection.getMaxPoolSize(),
            connection.getMinIdle(),
            connection.getConnectionTimeout(),
            connection.getCreatedAt(),
            connection.getUpdatedAt()
        );
    }

    public void update(DatabaseConnectionType connection) {
        String sql = """
            -- update
            UPDATE `app_connections`
            SET name = ?, database_type = ?, host = ?, port = ?, username = ?,
                password = ?, database_name = ?, connection_type = ?, ssh_host = ?,
                ssh_port = ?, ssh_username = ?, ssh_password = ?, ssh_private_key = ?,
                use_ssl = ?, ssl_cert_path = ?, http_tunnel_url = ?, color_hex = ?,
                max_pool_size = ?, min_idle = ?, connection_timeout = ?, updated_at = ?
            WHERE 1
            AND id = ?
            """;

        executeUpdate(
            sql,
            connection.getName(),
            connection.getDatabaseType().name(),
            connection.getHost(),
            connection.getPort(),
            connection.getUsername(),
            connection.getPassword(),
            connection.getDatabase(),
            connection.getConnectionType().name(),
            connection.getSshHost(),
            connection.getSshPort(),
            connection.getSshUsername(),
            connection.getSshPassword(),
            connection.getSshPrivateKey(),
            connection.getUseSsl(),
            connection.getSslCertPath(),
            connection.getHttpTunnelUrl(),
            connection.getColorHex(),
            connection.getMaxPoolSize(),
            connection.getMinIdle(),
            connection.getConnectionTimeout(),
            connection.getUpdatedAt(),
            connection.getId()
        );
    }

    public void deleteById(String id) {
        String sql = """
            -- deleteById
            DELETE FROM `app_connections`
            WHERE 1
            AND id = ?
            """;

        executeUpdate(sql, id);
    }
}
