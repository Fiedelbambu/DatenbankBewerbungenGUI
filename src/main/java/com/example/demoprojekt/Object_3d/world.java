package com.example.demoprojekt.Object_3d;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class world extends Application {

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private static final String EARTH_DIFFUSE_MAP = "/earth-d.jpg";
    private static final String EARTH_ILLUMINATION_MAP = "/earth-i.jpg";
    private static final String EARTH_SPECULAR_MAP = "/earth-aa.jpg";
    private static final String EARTH_BUMP_MAP = "/earth-b.jpg";

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-1000);

        Sphere sphere = prepareEarth();

        Group root = new Group();
        root.getChildren().add(sphere);
        root.getChildren().add(prepareImageView());

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        initMouseControl(sphere, scene, primaryStage);

        primaryStage.setTitle("3D Code");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation(sphere);
    }

    private void prepareAnimation(Sphere sphere) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.setRotationAxis(Rotate.Y_AXIS);
                sphere.setRotate(sphere.getRotate() + 0.05);
            }
        };
        timer.start();
    }

    private ImageView prepareImageView() {
        Image image = new Image(getClass().getResourceAsStream("/galaxy.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 0));
        return imageView;
    }

    private Sphere prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream(EARTH_DIFFUSE_MAP)));
        earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream(EARTH_ILLUMINATION_MAP)));
        earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream(EARTH_SPECULAR_MAP)));
        earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream(EARTH_BUMP_MAP)));

        Sphere sphere = new Sphere(150);
        sphere.setMaterial(earthMaterial);

        return sphere;
    }

    private void initMouseControl(Sphere sphere, Scene scene, Stage stage) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        sphere.getTransforms().addAll(xRotate, yRotate);

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            sphere.translateZProperty().set(sphere.getTranslateZ() + delta);
        });
    }

    public class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}
