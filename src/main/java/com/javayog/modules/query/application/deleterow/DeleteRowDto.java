package com.javayog.modules.query.application.deleterow;

import java.util.Map;

public record DeleteRowDto(
    String connectionId,
    String tableName,
    Map<String, Object> whereConditions
) {

    public static DeleteRowDto fromPrimitives(
        String connectionId,
        String tableName,
        Map<String, Object> whereConditions
    ) {
        return new DeleteRowDto(
            connectionId,
            tableName,
            whereConditions
        );
    }
}
