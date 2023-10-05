package com.example.demoprojekt.Object_3d;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Earth extends Application {

    private static final int WIDTH = 526;
    private static final int HEIGHTS = 314;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final Sphere sphere = new Sphere(150);

    private static final String EARTH_DIFFUSE_MAP = "/earth-d.jpg";
    private static final String EARTH_ILLUMINATION_MAP = "/earth-i.jpg";
    private static final String EARTH_SPECULAR_MAP = "/earth-aa.jpg";
    private static final String EARTH_BUMP_MAP = "/earth-b.jpg";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("3D Code");
        primaryStage.setScene(createScene(primaryStage));
        primaryStage.show();
    }

    private Scene createScene(Stage stage) {
        Camera camera = configureCamera();
        SmartGroup world = createWorld();
        Scene scene = new Scene(createRoot(world), WIDTH, HEIGHTS, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        initMouseControl(world, scene,stage);
        prepareAnimation();
        return scene;
    }



    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        final double sceneRotateSpeed = 0.3;
        final double sceneZoomSpeed = 2.0;

        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()) * sceneRotateSpeed);
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()) * sceneRotateSpeed);
        });

        scene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta * sceneZoomSpeed);
        });
    }


    private Camera configureCamera() {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-1000);
        return camera;
    }

    private SmartGroup createWorld() {
        SmartGroup world = new SmartGroup();
        world.getChildren().add(prepareEarth());
        return world;
    }

    private Group createRoot(Node... nodes) {
        Group root = new Group();
        root.getChildren().addAll(nodes);
        root.getChildren().add(prepareImageView());
        return root;
    }

    private void prepareAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.rotateProperty().set(sphere.getRotate() + 0.01);
            }
        };
        timer.start();
    }

    private ImageView prepareImageView() {
        Image image = new Image(Earth.class.getResourceAsStream("/galaxy.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 0));
        return imageView;
    }

    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream(EARTH_DIFFUSE_MAP)));
        earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream(EARTH_ILLUMINATION_MAP)));
        earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream(EARTH_SPECULAR_MAP)));
        earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream(EARTH_BUMP_MAP)));

        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);

        return sphere;
    }

    private void initMouseControl(SmartGroup group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneX();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorY - event.getSceneX());
        });

//        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
//            double delta = event.getDeltaY();
//            group.translateZProperty().set(group.getTranslateZ() + delta);
//        });
    }


    class SmartGroup extends Group {
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
