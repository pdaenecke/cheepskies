package org.cheepskies.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class MainApplication extends Application {
    public static MainController openMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationApplication.class.getResource("/org/gui/cheepskies/main-page.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        stage.setTitle("Main");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        return fxmlLoader.getController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        openMain();
    }
}
