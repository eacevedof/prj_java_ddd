package com.javayog.modules.query.application.exportresults;

public record ExportedResultsDto(
    String filePath,
    Integer rowCount,
    Long fileSizeBytes,
    String message
) {

    public static ExportedResultsDto from(String filePath, Integer rowCount, Long fileSizeBytes) {
        return new ExportedResultsDto(
            filePath,
            rowCount,
            fileSizeBytes,
            String.format("Results exported successfully to %s (%d rows)", filePath, rowCount)
        );
    }
}
