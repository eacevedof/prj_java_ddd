# JavaYog - Cross-Platform SQL Manager

A modern, cross-platform database management tool for MySQL and PostgreSQL, built with Java Spring Boot and JavaFX.

## Features

- **Multi-Database Support**: Connect to MySQL and PostgreSQL databases
- **Query Editor**: Execute SQL queries with syntax highlighting
- **Connection Management**: Save and manage multiple database connections
- **Export Results**: Export query results to CSV, JSON, XML, SQL, and HTML formats
- **Connection Types**: Support for direct connections, SSH tunneling, SSL, and HTTP tunneling
- **Connection Pooling**: HikariCP for efficient connection management

## Architecture

This project follows **Clean Architecture** (Hexagonal Architecture) principles with Domain-Driven Design (DDD):

```
com.javayog/
├── modules/
│   ├── shared/               # Shared kernel
│   │   ├── domain/
│   │   │   ├── enums/       # Shared enumerations
│   │   │   └── exceptions/  # Shared exceptions
│   │   └── infrastructure/
│   │       ├── components/  # Shared components
│   │       └── repositories/# Base repositories
│   ├── connection/          # Connection module
│   │   ├── application/     # Use cases (services + DTOs)
│   │   ├── domain/          # Domain models, exceptions, enums
│   │   └── infrastructure/  # Repositories, external services
│   └── query/               # Query module
│       ├── application/     # Use cases (services + DTOs)
│       ├── domain/          # Domain models, exceptions, enums
│       ├── infrastructure/  # Repositories, external services
│       └── presentation/    # JavaFX controllers
└── JavaYogApplication.java  # Spring Boot entry point
```

## Technology Stack

- **Java 21**: Modern Java with records and text blocks
- **Spring Boot 3.3**: Dependency injection and application framework
- **JavaFX 21**: Desktop UI framework
- **HikariCP**: High-performance JDBC connection pool
- **JSch**: SSH tunneling support
- **RichTextFX**: Advanced text editor for SQL syntax highlighting
- **Maven**: Build and dependency management

## Clean Code Practices

This project follows strict clean code conventions:

- **final classes**: All service and repository classes are final
- **records for DTOs**: All data transfer objects use Java records
- **Semantic naming**: Clear, descriptive variable and method names
- **Proper imports order**: Organized import statements
- **Text blocks for SQL**: Multi-line SQL queries use text blocks
- **WHERE 1 pattern**: SQL WHERE clauses start with `WHERE 1`
- **Factory methods**: DTOs use `from()` and `fromPrimitives()` patterns
- **JavaDoc**: All public methods have documentation

## Building and Running

### Prerequisites

- Java 21 or higher
- Maven 3.8+

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/java-yog-1.0.0.jar
```

## Configuration

Application configuration is in `src/main/resources/application.yml`:

- Spring Boot settings (no web server, desktop mode)
- HikariCP connection pool settings
- Query history and editor settings

## Database Schema

The application uses SQLite for storing connection configurations and query history. The schema is in `src/main/resources/database/schema.sql`.

Tables:
- `app_connections`: Database connection configurations
- `query_history`: Executed query history

## Module Structure

### Connection Module

Manages database connection configurations:

- **CreateConnection**: Create new connection configurations
- **UpdateConnection**: Update existing connections
- **DeleteConnection**: Delete connections
- **ConnectionReader**: Read connection configurations

### Query Module

Handles SQL query execution and result management:

- **ExecuteQuery**: Execute SQL queries
- **UpdateRow**: Update table rows
- **DeleteRow**: Delete table rows
- **ExportResults**: Export query results to various formats

## License

MIT License

## Author

JavaYog Development Team
