-- JavaYog Database Schema
-- SQLite database for storing application data (connections, query history, etc.)

CREATE TABLE IF NOT EXISTS `app_connections` (
    `id` VARCHAR(36) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `database_type` VARCHAR(50) NOT NULL,
    `host` VARCHAR(255) NOT NULL,
    `port` INTEGER NOT NULL,
    `username` VARCHAR(255),
    `password` VARCHAR(255),
    `database_name` VARCHAR(255),
    `connection_type` VARCHAR(50) NOT NULL DEFAULT 'DIRECT',
    `ssh_host` VARCHAR(255),
    `ssh_port` INTEGER,
    `ssh_username` VARCHAR(255),
    `ssh_password` VARCHAR(255),
    `ssh_private_key` TEXT,
    `use_ssl` BOOLEAN NOT NULL DEFAULT 0,
    `ssl_cert_path` VARCHAR(512),
    `http_tunnel_url` VARCHAR(512),
    `color_hex` VARCHAR(7),
    `max_pool_size` INTEGER NOT NULL DEFAULT 10,
    `min_idle` INTEGER NOT NULL DEFAULT 2,
    `connection_timeout` BIGINT NOT NULL DEFAULT 30000,
    `created_at` BIGINT NOT NULL,
    `updated_at` BIGINT NOT NULL
);

CREATE INDEX IF NOT EXISTS `idx_connections_name` ON `app_connections` (`name`);
CREATE INDEX IF NOT EXISTS `idx_connections_database_type` ON `app_connections` (`database_type`);

-- Query History Table
CREATE TABLE IF NOT EXISTS `query_history` (
    `id` VARCHAR(36) PRIMARY KEY,
    `connection_id` VARCHAR(36) NOT NULL,
    `sql_query` TEXT NOT NULL,
    `execution_time_millis` BIGINT,
    `row_count` INTEGER,
    `is_success` BOOLEAN NOT NULL,
    `error_message` TEXT,
    `executed_at` BIGINT NOT NULL,
    FOREIGN KEY (`connection_id`) REFERENCES `app_connections` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_query_history_connection` ON `query_history` (`connection_id`);
CREATE INDEX IF NOT EXISTS `idx_query_history_executed_at` ON `query_history` (`executed_at`);
