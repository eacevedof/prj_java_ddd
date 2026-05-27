package com.javayog.modules.query.domain.types;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QueryResultType {
    String queryId;
    String connectionId;
    String sqlQuery;
    List<String> columns;
    List<Map<String, Object>> rows;
    Integer rowCount;
    Long executionTimeMillis;
    Boolean isSuccess;
    String errorMessage;
    Long executedAt;
}
