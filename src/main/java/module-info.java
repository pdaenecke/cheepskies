module org.cheepskies.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires com.google.protobuf;


    opens org.cheepskies.ui to javafx.fxml;
    exports org.cheepskies.ui;
}