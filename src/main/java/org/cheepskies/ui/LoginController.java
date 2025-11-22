package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseUtils;

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
    private Label loginStatus;

    @FXML
    void loginAttempt(MouseEvent event) {
        String user = username.getText();
        String pass = password.getText();

        // checks if either field is empty
        if (user.isEmpty() || pass.isEmpty()) {
            loginStatus.setText("One or more fields are empty.");
            return;
        }

        // checks if username exists
        if (!DatabaseUtils.usernameScan(user)) {
            loginStatus.setText("Username does not exist.");
            return;
        }

        // password check
        if (!DatabaseUtils.loginValidation(user, pass)) {
            loginStatus.setText("Password is incorrect.");
            return;
        }

        // if any previous statement is not initialized, login is successful
        loginStatus.setText("Login successful!");

        ValueObject vo = new ValueObject();

    }

    @FXML
    void registrationRedirect(MouseEvent event) throws IOException {
        try {
            RegistrationApplication.openRegistration();
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}