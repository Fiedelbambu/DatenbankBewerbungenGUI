package com.example.demoprojekt.Object_3d;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Sphere3d extends Application {

    private static final int WIDTH = 1400;
    private static final int HEIGHTS = 800;

    @Override
    public void start(Stage primaryStage) throws  Exception{
        Sphere sphere = new Sphere(50);

        Group group = new Group();
        group.getChildren().add(sphere);

        Camera camera = new PerspectiveCamera(true);

        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-500);

        camera.setNearClip(1);
        camera.setFarClip(1000);

        /**
         * 3d Concept camera
         *      C       |   qwe asd          |   iusef(diesen wollen wir nicht sehen)
         *
         *Die Kamera geht zum Obeject
         */


        Scene scene = new Scene(group,WIDTH,HEIGHTS);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode())
            {
                case W:
                    camera.translateZProperty().set(camera.getTranslateZ() + 10);
                    break;
                case S:
                    camera.translateZProperty().set(camera.getTranslateZ() - 10);
                    break;
            }
        });

        primaryStage.setTitle("3d Code");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
        public static void main(String[] args) {
            launch(args);
        }
    }

