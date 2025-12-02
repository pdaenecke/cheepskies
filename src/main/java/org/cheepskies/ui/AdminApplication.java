package org.cheepskies.ui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AdminApplication.class.getResource("/org/gui/cheepskies/admin-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Admin Page");
        stage.setScene(scene);
        stage.show();
    }
}

