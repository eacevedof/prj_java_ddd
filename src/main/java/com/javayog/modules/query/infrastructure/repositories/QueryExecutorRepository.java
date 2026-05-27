package com.javayog.modules.query.infrastructure.repositories;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.connection.domain.types.DatabaseConnectionType;
import com.javayog.modules.connection.infrastructure.repositories.ConnectionReaderRepository;
import com.javayog.modules.query.domain.exceptions.QueryException;
import com.javayog.modules.query.infrastructure.components.DynamicDataSourceManager;

@Repository
@Slf4j
@RequiredArgsConstructor
public final class QueryExecutorRepository {

    private final ConnectionReaderRepository connectionReaderRepository;
    private final DynamicDataSourceManager dynamicDataSourceManager;

    /**
     * Executes a SQL query against the specified database connection
     *
     * @param connectionId database connection ID
     * @param sqlQuery SQL query to execute
     * @param maxRows maximum number of rows to return (null for unlimited)
     * @return list of rows as maps of column name to value
     * @throws QueryException if connection not found or query fails
     */
    public List<Map<String, Object>> executeQuery(
        String connectionId,
        String sqlQuery,
        Integer maxRows
    ) {
        log.debug("Executing query on connection {}: {}", connectionId, sqlQuery);

        DatabaseConnectionType databaseConnectionType = connectionReaderRepository.findById(connectionId)
            .orElseThrow(() -> QueryException.noConnectionSelected());

        DataSource dataSource = dynamicDataSourceManager.getDataSource(databaseConnectionType);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        if (maxRows != null && maxRows > 0) {
            jdbcTemplate.setMaxRows(maxRows);
        }

        try {
            return jdbcTemplate.queryForList(sqlQuery);
        } catch (Exception exception) {
            log.error("Query execution failed: {}", exception.getMessage());
            throw QueryException.executionFailed(exception.getMessage(), exception);
        }
    }

    /**
     * Executes an UPDATE or DELETE statement against the specified database connection
     *
     * @param connectionId database connection ID
     * @param sqlUpdate SQL update/delete statement to execute
     * @param params parameters for the prepared statement
     * @return number of rows affected
     * @throws QueryException if connection not found or update fails
     */
    public Integer executeUpdate(
        String connectionId,
        String sqlUpdate,
        Object... params
    ) {
        log.debug("Executing update on connection {}: {}", connectionId, sqlUpdate);

        DatabaseConnectionType databaseConnectionType = connectionReaderRepository.findById(connectionId)
            .orElseThrow(() -> QueryException.noConnectionSelected());

        DataSource dataSource = dynamicDataSourceManager.getDataSource(databaseConnectionType);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            return jdbcTemplate.update(sqlUpdate, params);
        } catch (Exception exception) {
            log.error("Update execution failed: {}", exception.getMessage());
            throw QueryException.updateFailed(exception.getMessage(), exception);
        }
    }
}
