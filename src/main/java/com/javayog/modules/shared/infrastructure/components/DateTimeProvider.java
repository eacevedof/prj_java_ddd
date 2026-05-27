package com.javayog.modules.shared.infrastructure.components;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Singleton component for date/time operations.
 * Similar to DateTimer in the reference project.
 */
@Component
public class DateTimeProvider {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Instant now() {
        return Instant.now();
    }

    public LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    public String nowFormatted() {
        return nowLocalDateTime().format(DEFAULT_FORMATTER);
    }

    public String format(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public String format(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDateTime fromEpochMilli(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    public long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
