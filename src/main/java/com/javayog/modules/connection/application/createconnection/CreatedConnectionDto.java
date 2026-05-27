package com.javayog.modules.connection.application.createconnection;

public record CreatedConnectionDto(
    String id,
    String name,
    String message,
    Long createdAt
) {

    public static CreatedConnectionDto from(String id, String name, Long createdAt) {
        return new CreatedConnectionDto(
            id,
            name,
            "Connection created successfully",
            createdAt
        );
    }
}
