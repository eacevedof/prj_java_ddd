package com.javayog.modules.query.application.deleterow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.query.domain.exceptions.QueryException;
import com.javayog.modules.query.infrastructure.repositories.QueryExecutorRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public final class DeleteRowService {

    private final QueryExecutorRepository queryExecutorRepository;

    /**
     * Deletes rows from a table based on where conditions
     *
     * @param deleteRowDto delete parameters including table and where clause
     * @return number of rows affected
     * @throws QueryException if delete fails
     */
    public DeletedRowDto execute(DeleteRowDto deleteRowDto) {
        log.info("Deleting rows from table: {}", deleteRowDto.tableName());

        if (deleteRowDto.whereConditions() == null || deleteRowDto.whereConditions().isEmpty()) {
            throw QueryException.deleteFailed("WHERE conditions are required for safety", null);
        }

        String sql = buildDeleteSql(deleteRowDto);
        List<Object> params = new ArrayList<>(deleteRowDto.whereConditions().values());

        try {
            Integer rowsAffected = queryExecutorRepository.executeUpdate(
                deleteRowDto.connectionId(),
                sql,
                params.toArray()
            );

            log.info("Rows deleted successfully: {} rows affected", rowsAffected);

            return DeletedRowDto.from(rowsAffected);

        } catch (Exception exception) {
            log.error("Delete failed: {}", exception.getMessage());
            throw QueryException.deleteFailed(exception.getMessage(), exception);
        }
    }

    private String buildDeleteSql(DeleteRowDto deleteRowDto) {
        StringBuilder sql = new StringBuilder("DELETE FROM `");
        sql.append(deleteRowDto.tableName()).append("` WHERE 1");

        deleteRowDto.whereConditions().keySet().forEach(key ->
            sql.append(" AND `").append(key).append("` = ?")
        );

        return sql.toString();
    }
}
