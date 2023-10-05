package com.example.demoprojekt.Object_3d;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Rotating3d_multithreading extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;
    private static final int ROTATION_SPEED = 5;

    private SmartGroup group;
    private boolean rotatingX = false;
    private boolean rotatingY = false;

    @Override
    public void start(Stage primaryStage) {
        Box box = new Box(100, 20, 50);
        group = new SmartGroup();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1200);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    group.translateZProperty().set(group.getTranslateZ() + 10);
                    break;
                case S:
                    group.translateZProperty().set(group.getTranslateZ() - 10);
                    break;
                case Q:
                    rotatingX = true;
                    break;
                case E:
                    rotatingX = true;
                    break;
                case NUMPAD6:
                    rotatingY = true;
                    break;
                case NUMPAD4:
                    rotatingY = true;
                    break;
            }
        });

        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case Q:
                    rotatingX = false;
                    break;
                case E:
                    rotatingX = false;
                    break;
                case NUMPAD6:
                    rotatingY = false;
                    break;
                case NUMPAD4:
                    rotatingY = false;
                    break;
            }
        });

        primaryStage.setTitle("3D Rotation mit Timeline");
        primaryStage.setScene(scene);
        primaryStage.show();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(50),
                event -> {
                    if (rotatingX) {
                        group.rotateByX(ROTATION_SPEED);
                    }
                    if (rotatingY) {
                        group.rotateByY(ROTATION_SPEED);
                    }
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    class SmartGroup extends Group {
        void rotateByX(double ang) {
            Rotate r = new Rotate(ang, Rotate.X_AXIS);
            getTransforms().add(r);
        }
        void rotateByY(double ang) {
            Rotate r = new Rotate(ang, Rotate.Y_AXIS);
            getTransforms().add(r);
        }
    }
}
