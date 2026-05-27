package com.javayog.modules.query.presentation.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.javayog.modules.connection.domain.types.DatabaseConnectionType;
import com.javayog.modules.connection.infrastructure.repositories.ConnectionReaderRepository;
import com.javayog.modules.query.application.executequery.ExecuteQueryDto;
import com.javayog.modules.query.application.executequery.ExecuteQueryService;
import com.javayog.modules.query.application.executequery.QueryResultDto;

@Controller
@Slf4j
@RequiredArgsConstructor
public final class MainWindowController {

    private final ConnectionReaderRepository connectionReaderRepository;
    private final ExecuteQueryService executeQueryService;

    @FXML
    private ListView<DatabaseConnectionType> connectionListView;

    @FXML
    private TextArea queryTextArea;

    @FXML
    private TextField maxRowsTextField;

    @FXML
    private TableView<Map<String, Object>> resultTableView;

    @FXML
    private TextArea statusTextArea;

    private DatabaseConnectionType selectedConnection;

    @FXML
    public void initialize() {
        log.info("Initializing MainWindowController");
        loadConnections();
        setupConnectionListView();
    }

    @FXML
    private void handleNewConnection() {
        log.info("New connection requested");
        showAlert("New Connection", "Connection dialog not yet implemented");
    }

    @FXML
    private void handleEditConnection() {
        if (selectedConnection == null) {
            showAlert("Edit Connection", "Please select a connection first");
            return;
        }
        log.info("Edit connection requested: {}", selectedConnection.getName());
        showAlert("Edit Connection", "Connection dialog not yet implemented");
    }

    @FXML
    private void handleDeleteConnection() {
        if (selectedConnection == null) {
            showAlert("Delete Connection", "Please select a connection first");
            return;
        }
        log.info("Delete connection requested: {}", selectedConnection.getName());
        showAlert("Delete Connection", "Delete functionality not yet implemented");
    }

    @FXML
    private void handleExecuteQuery() {
        if (selectedConnection == null) {
            showAlert("Execute Query", "Please select a connection first");
            return;
        }

        String sqlQuery = queryTextArea.getText();
        if (sqlQuery == null || sqlQuery.isBlank()) {
            showAlert("Execute Query", "Please enter a SQL query");
            return;
        }

        log.info("Executing query on connection: {}", selectedConnection.getName());

        try {
            Integer maxRows = parseMaxRows();
            ExecuteQueryDto executeQueryDto = ExecuteQueryDto.fromPrimitives(
                selectedConnection.getId(),
                sqlQuery,
                maxRows
            );

            long startTime = System.currentTimeMillis();
            QueryResultDto queryResultDto = executeQueryService.execute(executeQueryDto);
            long executionTime = System.currentTimeMillis() - startTime;

            displayResults(queryResultDto);
            updateStatus(String.format(
                "Query executed successfully. %d rows returned in %d ms",
                queryResultDto.rowCount(),
                executionTime
            ));

        } catch (Exception exception) {
            log.error("Query execution failed: {}", exception.getMessage());
            showAlert("Query Error", exception.getMessage());
            updateStatus("Query execution failed: " + exception.getMessage());
        }
    }

    @FXML
    private void handleExportResults() {
        log.info("Export results requested");
        showAlert("Export Results", "Export functionality not yet implemented");
    }

    @FXML
    private void handleRefreshConnections() {
        log.info("Refresh connections requested");
        loadConnections();
        updateStatus("Connections refreshed");
    }

    private void loadConnections() {
        List<DatabaseConnectionType> connections = connectionReaderRepository.findAll();
        ObservableList<DatabaseConnectionType> items = FXCollections.observableArrayList(connections);
        connectionListView.setItems(items);
        log.info("Loaded {} connections", connections.size());
    }

    private void setupConnectionListView() {
        connectionListView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(DatabaseConnectionType connection, boolean empty) {
                super.updateItem(connection, empty);
                if (empty || connection == null) {
                    setText(null);
                } else {
                    setText(String.format("%s (%s)", connection.getName(), connection.getDatabaseType().getDisplayName()));
                }
            }
        });

        connectionListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedConnection = newValue;
                if (newValue != null) {
                    log.info("Connection selected: {}", newValue.getName());
                    updateStatus("Connected to: " + newValue.getName());
                }
            }
        );
    }

    private void displayResults(QueryResultDto queryResultDto) {
        resultTableView.getColumns().clear();
        resultTableView.getItems().clear();

        if (queryResultDto.columns().isEmpty()) {
            return;
        }

        // Create columns
        for (String columnName : queryResultDto.columns()) {
            TableColumn<Map<String, Object>, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(cellData -> {
                Object value = cellData.getValue().get(columnName);
                return new javafx.beans.property.SimpleStringProperty(
                    value != null ? value.toString() : ""
                );
            });
            resultTableView.getColumns().add(column);
        }

        // Add rows
        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList(
            queryResultDto.rows()
        );
        resultTableView.setItems(items);
    }

    private Integer parseMaxRows() {
        String maxRowsText = maxRowsTextField.getText();
        if (maxRowsText == null || maxRowsText.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(maxRowsText);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private void updateStatus(String message) {
        statusTextArea.setText(message);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
