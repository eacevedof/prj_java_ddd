package com.javayog.modules.query.application.exportresults;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.query.domain.exceptions.QueryException;
import com.javayog.modules.query.domain.enums.ExportFormatEnum;

@Service
@Slf4j
@RequiredArgsConstructor
public final class ExportResultsService {

    /**
     * Exports query results to a file in the specified format
     *
     * @param exportResultsDto export parameters including file path, format, and data
     * @return export metadata including file size and row count
     * @throws QueryException if export fails
     */
    public ExportedResultsDto execute(ExportResultsDto exportResultsDto) {
        log.info("Exporting results to: {}", exportResultsDto.filePath());

        if (exportResultsDto.rows() == null || exportResultsDto.rows().isEmpty()) {
            throw QueryException.exportFailed("No data to export", null);
        }

        try {
            switch (exportResultsDto.exportFormat()) {
                case CSV -> exportToCsv(exportResultsDto);
                case JSON -> exportToJson(exportResultsDto);
                case XML -> exportToXml(exportResultsDto);
                case SQL -> exportToSql(exportResultsDto);
                case HTML -> exportToHtml(exportResultsDto);
            }

            Path path = Path.of(exportResultsDto.filePath());
            Long fileSize = Files.size(path);
            Integer rowCount = exportResultsDto.rows().size();

            log.info("Export completed: {} rows, {} bytes", rowCount, fileSize);

            return ExportedResultsDto.from(exportResultsDto.filePath(), rowCount, fileSize);

        } catch (Exception exception) {
            log.error("Export failed: {}", exception.getMessage());
            throw QueryException.exportFailed(exception.getMessage(), exception);
        }
    }

    private void exportToCsv(ExportResultsDto exportResultsDto) throws IOException {
        try (FileWriter writer = new FileWriter(exportResultsDto.filePath())) {
            // Write header
            writer.write(String.join(",", exportResultsDto.columns()));
            writer.write("\n");

            // Write rows
            for (var row : exportResultsDto.rows()) {
                StringBuilder line = new StringBuilder();
                for (String column : exportResultsDto.columns()) {
                    Object value = row.get(column);
                    String escapedValue = escapeCsvValue(value);
                    line.append(escapedValue).append(",");
                }
                line.deleteCharAt(line.length() - 1); // Remove trailing comma
                writer.write(line.toString());
                writer.write("\n");
            }
        }
    }

    private void exportToJson(ExportResultsDto exportResultsDto) throws IOException {
        try (FileWriter writer = new FileWriter(exportResultsDto.filePath())) {
            writer.write("[\n");
            for (int i = 0; i < exportResultsDto.rows().size(); i++) {
                var row = exportResultsDto.rows().get(i);
                writer.write("  {\n");
                int j = 0;
                for (String column : exportResultsDto.columns()) {
                    Object value = row.get(column);
                    String jsonValue = escapeJsonValue(value);
                    writer.write(String.format("    \"%s\": %s", column, jsonValue));
                    if (j < exportResultsDto.columns().size() - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                    j++;
                }
                writer.write("  }");
                if (i < exportResultsDto.rows().size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]\n");
        }
    }

    private void exportToXml(ExportResultsDto exportResultsDto) throws IOException {
        try (FileWriter writer = new FileWriter(exportResultsDto.filePath())) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<resultset>\n");
            for (var row : exportResultsDto.rows()) {
                writer.write("  <row>\n");
                for (String column : exportResultsDto.columns()) {
                    Object value = row.get(column);
                    String xmlValue = escapeXmlValue(value);
                    writer.write(String.format("    <%s>%s</%s>\n", column, xmlValue, column));
                }
                writer.write("  </row>\n");
            }
            writer.write("</resultset>\n");
        }
    }

    private void exportToSql(ExportResultsDto exportResultsDto) throws IOException {
        try (FileWriter writer = new FileWriter(exportResultsDto.filePath())) {
            String tableName = exportResultsDto.tableName() != null
                ? exportResultsDto.tableName()
                : "exported_table";

            for (var row : exportResultsDto.rows()) {
                writer.write(String.format("INSERT INTO `%s` (", tableName));
                writer.write(String.join(", ", exportResultsDto.columns().stream()
                    .map(col -> "`" + col + "`")
                    .toList()));
                writer.write(") VALUES (");

                StringBuilder values = new StringBuilder();
                for (String column : exportResultsDto.columns()) {
                    Object value = row.get(column);
                    String sqlValue = escapeSqlValue(value);
                    values.append(sqlValue).append(", ");
                }
                values.delete(values.length() - 2, values.length()); // Remove trailing ", "
                writer.write(values.toString());
                writer.write(");\n");
            }
        }
    }

    private void exportToHtml(ExportResultsDto exportResultsDto) throws IOException {
        try (FileWriter writer = new FileWriter(exportResultsDto.filePath())) {
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n");
            writer.write("<meta charset=\"UTF-8\">\n");
            writer.write("<title>Query Results</title>\n");
            writer.write("<style>table{border-collapse:collapse;width:100%;}");
            writer.write("th,td{border:1px solid #ddd;padding:8px;text-align:left;}");
            writer.write("th{background-color:#4CAF50;color:white;}</style>\n");
            writer.write("</head>\n<body>\n<table>\n<thead>\n<tr>\n");

            // Write header
            for (String column : exportResultsDto.columns()) {
                writer.write(String.format("<th>%s</th>\n", escapeHtmlValue(column)));
            }
            writer.write("</tr>\n</thead>\n<tbody>\n");

            // Write rows
            for (var row : exportResultsDto.rows()) {
                writer.write("<tr>\n");
                for (String column : exportResultsDto.columns()) {
                    Object value = row.get(column);
                    writer.write(String.format("<td>%s</td>\n", escapeHtmlValue(value)));
                }
                writer.write("</tr>\n");
            }

            writer.write("</tbody>\n</table>\n</body>\n</html>\n");
        }
    }

    private String escapeCsvValue(Object value) {
        if (value == null) return "";
        String str = value.toString();
        if (str.contains(",") || str.contains("\"") || str.contains("\n")) {
            return "\"" + str.replace("\"", "\"\"") + "\"";
        }
        return str;
    }

    private String escapeJsonValue(Object value) {
        if (value == null) return "null";
        if (value instanceof Number) return value.toString();
        if (value instanceof Boolean) return value.toString();
        String str = value.toString();
        str = str.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
        return "\"" + str + "\"";
    }

    private String escapeXmlValue(Object value) {
        if (value == null) return "";
        return value.toString()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }

    private String escapeSqlValue(Object value) {
        if (value == null) return "NULL";
        if (value instanceof Number) return value.toString();
        String str = value.toString();
        str = str.replace("'", "''");
        return "'" + str + "'";
    }

    private String escapeHtmlValue(Object value) {
        if (value == null) return "";
        return value.toString()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
}
