package com.example.demoprojekt;


import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class LoginController implements Initializable {

    @FXML
    public AnchorPane AncohrPane_main;
    @FXML
    public AnchorPane anchorePane;

    @FXML
    private TextField adresse_textField;

    @FXML
    private TextField benutzername_textField;

    @FXML
    public TextField passwort_textField;

    @FXML
    private Button loginButton;

    @FXML
    private Sphere sphere_earth;


    /////////////////////////////////////////////////////////////
    private static final String EARTH_DIFFUSE_MAP = "/earth-d.jpg";
    private static final String EARTH_ILLUMINATION_MAP = "/earth-i.jpg";
    private static final String EARTH_SPECULAR_MAP = "/earth-aa.jpg";
    private static final String EARTH_BUMP_MAP = "/earth-b.jpg";




    ///////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

///////////////////// Konfigurieren der sphere//////////////////////////////////


        sphere_earth.setTranslateX(438);
        sphere_earth.setTranslateY(136);
        sphere_earth.setTranslateZ(0);


        double paneWidth = AncohrPane_main.getWidth();
        double paneHeight = AncohrPane_main.getHeight();
        double sphereY = 315;
        double sphereX = (paneWidth - 2 * sphere_earth.getRadius()) / 2 + 100 ;

        sphere_earth.setTranslateX(sphereX);
        sphere_earth.setTranslateY(sphereY);


        sphere_earth.setTranslateX(sphereX);
        sphere_earth.setTranslateY(sphereY);

        Image diffuseMap = new Image(EARTH_DIFFUSE_MAP);
        PhongMaterial material = new PhongMaterial(Color.SILVER);
        sphere_earth.setMaterial(material);
        material.setDiffuseMap(diffuseMap);


        AncohrPane_main.getChildren().add(sphere_earth);


        prepareAnimation();
        Camera camera = configureCamera();
        anchorePane.getChildren().add(new Group(camera));


//////////////////////////////////////////////////////////////////////////////////////////////


        loginButton.setOnAction(this::handleLogin);

        adresse_textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

        benutzername_textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

        passwort_textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

    }


        private void handleLogin(ActionEvent event) {
        String hostname = adresse_textField.getText();
        String username = benutzername_textField.getText();
        String password = passwort_textField.getText();

        // Perform the login logic and display the result in loggedIn
        boolean loggedIn = login(hostname, username, password);

        if (loggedIn) {
            // Redirect to the next scene upon successful login
            loadNextScene(event);
        } else {
            // Display an alert for failed login
            showAlert("Anmeldung fehlgeschlagen", "Benutzername oder Passwort ist falsch.");
        }

           }
    public boolean login(String hostname, String setUsername, String setPassword) {
        DatabaseConfig dbConfig = new DatabaseConfig("set.text.username", "set.text.password");

        dbConfig.setUsername(setUsername);
        dbConfig.setPassword(setPassword);

        String jdbcUrl = "jdbc:mysql://" + hostname + ":3306/bewerbung";
        String dbUsername = dbConfig.getUsername();
        String dbPassword = dbConfig.getPassword();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {

            boolean loggedIn = true; // Simulierte erfolgreiche Anmeldung

            return loggedIn;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadNextScene(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demoprojekt/datenbankbewerbungen.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 880, 735);
            stage.setTitle("Datenbankbewerbungen");
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - 400) / 2;
            double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - 600) / 2;
            stage.setX(centerX);
            stage.setY(centerY);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Fehler beim Laden der Szene", "Die Szene konnte nicht geladen werden: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Methoden für die Sphere /////////////////////////////////////////////////////
    private Camera configureCamera() {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-1000);
        return camera;
    }

    private void prepareAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere_earth.setRotationAxis(Rotate.Y_AXIS);
                sphere_earth.rotateProperty().set(sphere_earth.getRotate() - 0.004);


            }
        };
        timer.start();
    }


    }










