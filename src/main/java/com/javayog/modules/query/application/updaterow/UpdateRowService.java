package com.javayog.modules.query.application.updaterow;

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
public final class UpdateRowService {

    private final QueryExecutorRepository queryExecutorRepository;

    /**
     * Updates rows in a table based on where conditions
     *
     * @param updateRowDto update parameters including table, new values, and where clause
     * @return number of rows affected
     * @throws QueryException if update fails
     */
    public UpdatedRowDto execute(UpdateRowDto updateRowDto) {
        log.info("Updating rows in table: {}", updateRowDto.tableName());

        if (updateRowDto.newValues() == null || updateRowDto.newValues().isEmpty()) {
            throw QueryException.updateFailed("No values to update", null);
        }

        if (updateRowDto.whereConditions() == null || updateRowDto.whereConditions().isEmpty()) {
            throw QueryException.updateFailed("WHERE conditions are required for safety", null);
        }

        String sql = buildUpdateSql(updateRowDto);
        List<Object> params = buildParams(updateRowDto);

        try {
            Integer rowsAffected = queryExecutorRepository.executeUpdate(
                updateRowDto.connectionId(),
                sql,
                params.toArray()
            );

            log.info("Rows updated successfully: {} rows affected", rowsAffected);

            return UpdatedRowDto.from(rowsAffected);

        } catch (Exception exception) {
            log.error("Update failed: {}", exception.getMessage());
            throw QueryException.updateFailed(exception.getMessage(), exception);
        }
    }

    private String buildUpdateSql(UpdateRowDto updateRowDto) {
        StringBuilder sql = new StringBuilder("UPDATE `");
        sql.append(updateRowDto.tableName()).append("` SET ");

        // SET clause
        List<String> setClauses = new ArrayList<>();
        updateRowDto.newValues().keySet().forEach(key ->
            setClauses.add("`" + key + "` = ?")
        );
        sql.append(String.join(", ", setClauses));

        // WHERE clause
        sql.append(" WHERE 1");
        updateRowDto.whereConditions().keySet().forEach(key ->
            sql.append(" AND `").append(key).append("` = ?")
        );

        return sql.toString();
    }

    private List<Object> buildParams(UpdateRowDto updateRowDto) {
        List<Object> params = new ArrayList<>();
        params.addAll(updateRowDto.newValues().values());
        params.addAll(updateRowDto.whereConditions().values());
        return params;
    }
}
