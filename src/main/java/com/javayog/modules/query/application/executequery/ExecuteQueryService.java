package com.javayog.modules.query.application.executequery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.shared.infrastructure.components.DateTimeProvider;

import com.javayog.modules.query.domain.exceptions.QueryException;
import com.javayog.modules.query.infrastructure.repositories.QueryExecutorRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public final class ExecuteQueryService {

    private final QueryExecutorRepository queryExecutorRepository;
    private final DateTimeProvider dateTimeProvider;

    /**
     * Executes a SQL query against the specified database connection
     *
     * @param executeQueryDto query parameters including connection ID and SQL
     * @return query results with columns, rows, and execution metadata
     * @throws QueryException if connection is invalid or query execution fails
     */
    public QueryResultDto execute(ExecuteQueryDto executeQueryDto) {
        log.info("Executing query on connection: {}", executeQueryDto.connectionId());

        if (executeQueryDto.sqlQuery() == null || executeQueryDto.sqlQuery().isBlank()) {
            throw QueryException.invalidSyntax("SQL query cannot be empty");
        }

        String queryId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        try {
            List<Map<String, Object>> rows = queryExecutorRepository.executeQuery(
                executeQueryDto.connectionId(),
                executeQueryDto.sqlQuery(),
                executeQueryDto.maxRows()
            );

            List<String> columns = rows.isEmpty()
                ? List.of()
                : List.copyOf(rows.get(0).keySet());

            long executionTime = System.currentTimeMillis() - startTime;

            log.info("Query executed successfully: {} rows in {}ms", rows.size(), executionTime);

            return QueryResultDto.from(queryId, columns, rows, executionTime);

        } catch (Exception exception) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Query execution failed after {}ms: {}", executionTime, exception.getMessage());
            throw QueryException.executionFailed(exception.getMessage(), exception);
        }
    }
}
