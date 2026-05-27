package com.javayog.modules.query.application.deleterow;

public record DeletedRowDto(
    Integer rowsAffected,
    String message
) {

    public static DeletedRowDto from(Integer rowsAffected) {
        return new DeletedRowDto(
            rowsAffected,
            String.format("Row deleted successfully. %d rows affected", rowsAffected)
        );
    }
}
