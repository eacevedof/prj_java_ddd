package com.javayog.modules.query.application.executequery;

import java.util.List;
import java.util.Map;

public record QueryResultDto(
    String queryId,
    List<String> columns,
    List<Map<String, Object>> rows,
    Integer rowCount,
    Long executionTimeMillis,
    String message
) {

    public static QueryResultDto from(
        String queryId,
        List<String> columns,
        List<Map<String, Object>> rows,
        Long executionTimeMillis
    ) {
        return new QueryResultDto(
            queryId,
            columns,
            rows,
            rows.size(),
            executionTimeMillis,
            String.format("Query executed successfully. %d rows returned", rows.size())
        );
    }
}
