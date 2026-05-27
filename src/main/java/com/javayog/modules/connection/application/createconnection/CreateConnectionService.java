package com.javayog.modules.connection.application.createconnection;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.shared.infrastructure.components.DateTimeProvider;

import com.javayog.modules.connection.domain.types.DatabaseConnectionType;
import com.javayog.modules.connection.infrastructure.repositories.ConnectionWriterRepository;
import com.javayog.modules.connection.domain.exceptions.ConnectionException;

@Service
@Slf4j
@RequiredArgsConstructor
public final class CreateConnectionService {

    private final ConnectionWriterRepository connectionWriterRepository;
    private final DateTimeProvider dateTimeProvider;

    /**
     * @throws ConnectionException if connection name already exists
     */
    public CreatedConnectionDto execute(CreateConnectionDto createConnectionDto) {
        log.info("Creating connection: {}", createConnectionDto.name());

        if (connectionWriterRepository.existsByName(createConnectionDto.name()))
            throw ConnectionException.alreadyExists(createConnectionDto.name());

        Long now = dateTimeProvider.now().toEpochMilli();
        String id = UUID.randomUUID().toString();

        DatabaseConnectionType connection = DatabaseConnectionType.builder()
            .id(id)
            .name(createConnectionDto.name())
            .databaseType(createConnectionDto.databaseType())
            .host(createConnectionDto.host())
            .port(createConnectionDto.port())
            .username(createConnectionDto.username())
            .password(createConnectionDto.password())
            .database(createConnectionDto.database())
            .connectionType(createConnectionDto.connectionType())
            .sshHost(createConnectionDto.sshHost())
            .sshPort(createConnectionDto.sshPort())
            .sshUsername(createConnectionDto.sshUsername())
            .sshPassword(createConnectionDto.sshPassword())
            .sshPrivateKey(createConnectionDto.sshPrivateKey())
            .useSsl(createConnectionDto.useSsl())
            .sslCertPath(createConnectionDto.sslCertPath())
            .httpTunnelUrl(createConnectionDto.httpTunnelUrl())
            .colorHex(createConnectionDto.colorHex())
            .maxPoolSize(10)
            .minIdle(2)
            .connectionTimeout(30000L)
            .createdAt(now)
            .updatedAt(now)
            .build();

        connectionWriterRepository.save(connection);

        log.info("Connection created successfully: {}", id);

        return CreatedConnectionDto.from(id, createConnectionDto.name(), now);
    }
}
