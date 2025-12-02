package org.cheepskies.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginApplication extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/org/gui/cheepskies/login-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1004, 725);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

