package com.javayog;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application entry point for JavaYog.
 * Delegates UI startup to JavaFxApplication.
 */
@SpringBootApplication
public class JavaYogApplication {

    public static void main(String[] args) {
        // Launch JavaFX which will bootstrap Spring context
        Application.launch(JavaFxApplication.class, args);
    }
}
