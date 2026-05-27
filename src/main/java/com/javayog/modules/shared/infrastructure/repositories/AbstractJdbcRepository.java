package com.javayog.modules.shared.infrastructure.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract base repository for JDBC operations.
 * Provides common database access patterns.
 * Similar to AbstractPostgresRepository in the reference project.
 */
@Slf4j
public abstract class AbstractJdbcRepository {

    protected final JdbcTemplate jdbcTemplate;

    protected AbstractJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Execute a query and return multiple rows
     */
    protected List<Map<String, Object>> executeQuery(String sql, Object... params) {
        log.debug("Executing query: {} with params: {}", sql, params);
        return jdbcTemplate.queryForList(sql, params);
    }

    /**
     * Execute a query and return a single row
     */
    protected Optional<Map<String, Object>> executeSingleQuery(String sql, Object... params) {
        log.debug("Executing single query: {} with params: {}", sql, params);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Execute an update/insert/delete statement
     */
    protected int executeUpdate(String sql, Object... params) {
        log.debug("Executing update: {} with params: {}", sql, params);
        return jdbcTemplate.update(sql, params);
    }

    /**
     * Execute a batch update
     */
    protected int[] executeBatch(String sql, List<Object[]> batchArgs) {
        log.debug("Executing batch update: {} with {} rows", sql, batchArgs.size());
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * Map column to integer, handling null values
     */
    protected Integer mapColumnToInt(Map<String, Object> row, String columnName) {
        Object value = row.get(columnName);
        if (value == null) return null;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * Map column to string, handling null values
     */
    protected String mapColumnToString(Map<String, Object> row, String columnName) {
        Object value = row.get(columnName);
        return value == null ? null : value.toString();
    }

    /**
     * Check if a record exists
     */
    protected boolean exists(String tableName, String whereClause, Object... params) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s", tableName, whereClause);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, params);
        return count != null && count > 0;
    }
}
