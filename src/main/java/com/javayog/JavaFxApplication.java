package com.javayog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * JavaFX Application that integrates with Spring Boot.
 * Initializes Spring context and loads the main UI.
 */
public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        // Initialize Spring Boot context
        springContext = SpringApplication.run(JavaYogApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        // Load main view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-window.fxml"));
        loader.setControllerFactory(springContext::getBean); // Enable Spring DI in controllers

        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 800);

        // Apply CSS
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("JavaYog - SQL Manager");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> closeApplication());
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Gracefully shutdown Spring context
        springContext.close();
        Platform.exit();
    }

    private void closeApplication() {
        Platform.exit();
    }
}
