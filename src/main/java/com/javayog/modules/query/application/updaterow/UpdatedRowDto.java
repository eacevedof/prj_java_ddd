package com.javayog.modules.query.application.updaterow;

public record UpdatedRowDto(
    Integer rowsAffected,
    String message
) {

    public static UpdatedRowDto from(Integer rowsAffected) {
        return new UpdatedRowDto(
            rowsAffected,
            String.format("Row updated successfully. %d rows affected", rowsAffected)
        );
    }
}
