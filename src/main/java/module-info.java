module com.example.demoprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;




    opens com.example.demoprojekt to javafx.fxml;
    exports com.example.demoprojekt;
    exports com.example.demoprojekt.Object_3d;

}