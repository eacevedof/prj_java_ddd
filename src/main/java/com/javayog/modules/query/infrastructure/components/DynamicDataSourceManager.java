package com.javayog.modules.query.infrastructure.components;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.connection.domain.types.DatabaseConnectionType;
import com.javayog.modules.shared.domain.enums.DatabaseTypeEnum;

/**
 * Manages dynamic DataSource creation and caching for database connections.
 * Creates HikariCP connection pools on-demand for each connection configuration.
 */
@Component
@Slf4j
public final class DynamicDataSourceManager {

    private final Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    /**
     * Gets or creates a DataSource for the given connection configuration
     *
     * @param databaseConnectionType connection configuration
     * @return DataSource ready for query execution
     */
    public DataSource getDataSource(DatabaseConnectionType databaseConnectionType) {
        String connectionId = databaseConnectionType.getId();

        return dataSourceCache.computeIfAbsent(connectionId, id -> {
            log.info("Creating new DataSource for connection: {}", databaseConnectionType.getName());
            return createDataSource(databaseConnectionType);
        });
    }

    /**
     * Removes a DataSource from cache and closes it
     *
     * @param connectionId connection ID to remove
     */
    public void removeDataSource(String connectionId) {
        DataSource dataSource = dataSourceCache.remove(connectionId);
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            log.info("Closing DataSource for connection: {}", connectionId);
            hikariDataSource.close();
        }
    }

    /**
     * Closes all cached DataSources
     */
    public void closeAll() {
        log.info("Closing all DataSources: {} connections", dataSourceCache.size());
        dataSourceCache.values().forEach(dataSource -> {
            if (dataSource instanceof HikariDataSource hikariDataSource) {
                hikariDataSource.close();
            }
        });
        dataSourceCache.clear();
    }

    private DataSource createDataSource(DatabaseConnectionType databaseConnectionType) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(buildJdbcUrl(databaseConnectionType));
        config.setUsername(databaseConnectionType.getUsername());
        config.setPassword(databaseConnectionType.getPassword());
        config.setDriverClassName(getDriverClassName(databaseConnectionType.getDatabaseType()));

        // Connection pool settings
        config.setMaximumPoolSize(databaseConnectionType.getMaxPoolSize());
        config.setMinimumIdle(databaseConnectionType.getMinIdle());
        config.setConnectionTimeout(databaseConnectionType.getConnectionTimeout());
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes

        // Connection pool name for debugging
        config.setPoolName(databaseConnectionType.getName() + "-pool");

        return new HikariDataSource(config);
    }

    private String buildJdbcUrl(DatabaseConnectionType databaseConnectionType) {
        String protocol = getJdbcProtocol(databaseConnectionType.getDatabaseType());
        String host = databaseConnectionType.getHost();
        Integer port = databaseConnectionType.getPort();
        String database = databaseConnectionType.getDatabase();

        // TODO: Add support for SSH tunnel, SSL, and HTTP tunnel
        return String.format("%s://%s:%d/%s", protocol, host, port, database);
    }

    private String getJdbcProtocol(DatabaseTypeEnum databaseTypeEnum) {
        return switch (databaseTypeEnum) {
            case MYSQL -> "jdbc:mysql";
            case POSTGRESQL -> "jdbc:postgresql";
        };
    }

    private String getDriverClassName(DatabaseTypeEnum databaseTypeEnum) {
        return switch (databaseTypeEnum) {
            case MYSQL -> "com.mysql.cj.jdbc.Driver";
            case POSTGRESQL -> "org.postgresql.Driver";
        };
    }
}
