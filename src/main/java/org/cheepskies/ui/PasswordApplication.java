package org.cheepskies.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PasswordApplication {
    public static void openPassword() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationApplication.class.getResource("/org/gui/cheepskies/password-page.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        stage.setTitle("Password Recovery");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
