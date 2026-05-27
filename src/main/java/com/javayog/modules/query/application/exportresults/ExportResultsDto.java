package com.javayog.modules.query.application.exportresults;

import java.util.List;
import java.util.Map;

import com.javayog.modules.query.domain.enums.ExportFormatEnum;

public record ExportResultsDto(
    String filePath,
    ExportFormatEnum exportFormat,
    List<String> columns,
    List<Map<String, Object>> rows,
    String tableName
) {

    public static ExportResultsDto fromPrimitives(
        String filePath,
        String exportFormat,
        List<String> columns,
        List<Map<String, Object>> rows,
        String tableName
    ) {
        return new ExportResultsDto(
            filePath,
            ExportFormatEnum.fromString(exportFormat),
            columns,
            rows,
            tableName
        );
    }
}
