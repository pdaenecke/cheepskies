package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button login;

    @FXML
    private TextField password;

    @FXML
    private Button register;

    @FXML
    private TextField username;

    @FXML
    void registrationRedirect(MouseEvent event) throws IOException {
        try {
            RegistrationWindow.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}