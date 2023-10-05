package com.example.demoprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Lade die login.fxml-Datei und erstelle einen neuen Controller
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 880, 735);

        stage.setTitle("Login"); // Setze den Titel des Fensters

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Berechnen Sie die Position, um das Fenster in der Mitte zu platzieren
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - 335) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - 600) / 2;

        // Setzen Sie die Position des Fensters
        stage.setX(centerX);
        stage.setY(centerY);



        stage.setScene(scene); // Setze die Szene für das Fenster
        stage.show(); // Zeige das Fenster

    }

    public static void main(String[] args) {
        launch();
    }
}



