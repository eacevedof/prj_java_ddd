package com.javayog.modules.query.application.updaterow;

import java.util.Map;

public record UpdateRowDto(
    String connectionId,
    String tableName,
    Map<String, Object> newValues,
    Map<String, Object> whereConditions
) {

    public static UpdateRowDto fromPrimitives(
        String connectionId,
        String tableName,
        Map<String, Object> newValues,
        Map<String, Object> whereConditions
    ) {
        return new UpdateRowDto(
            connectionId,
            tableName,
            newValues,
            whereConditions
        );
    }
}
