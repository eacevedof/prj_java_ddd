package com.javayog.modules.query.application.executequery;

public record ExecuteQueryDto(
    String connectionId,
    String sqlQuery,
    Integer maxRows
) {

    public static ExecuteQueryDto fromPrimitives(
        String connectionId,
        String sqlQuery,
        Integer maxRows
    ) {
        return new ExecuteQueryDto(
            connectionId,
            sqlQuery,
            maxRows
        );
    }
}
