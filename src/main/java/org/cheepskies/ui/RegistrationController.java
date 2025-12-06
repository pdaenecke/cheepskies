package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.cheepskiesdb.DatabaseUtils;

public class RegistrationController {

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField middleInitial;

    @FXML
    private TextField password;

    @FXML
    private Button register;

    @FXML
    private Label registrationStatus;

    @FXML
    private TextField username;

    @FXML
    private TextField security;

    @FXML
    void accountCreation(MouseEvent event) {

        String first = firstName.getText();
        String last = lastName.getText();
        String middle = middleInitial.getText();
        String user = username.getText();
        String pass = password.getText();
        String mail = email.getText();
        String secure = security.getText();

        if (first.isEmpty() || last.isEmpty() || middle.isEmpty() ||
                user.isEmpty() || pass.isEmpty() || mail.isEmpty() || secure.isEmpty()) {
            registrationStatus.setText("One or more fields are empty.");
            return;
        }

        if (middle.length() > 1) {
            registrationStatus.setText("Middle initial is more than one character.");
            return;
        }

        ValueObject vo = new ValueObject();
        Customer c = vo.getCustomer();

        c.setFirstName(first);
        c.setLastName(last);
        c.setmI(middle);
        c.setUsername(user);
        c.setPassword(pass);
        c.setEmail(mail);
        c.setAnswer(secure);

        vo.setAction("register");

        try {
            Facade.process(vo);

            if (vo.operationResult) {
                registrationStatus.setText("Registration complete.");
            } else {
                registrationStatus.setText("Registration failed.");  // error message from facade
            }

        } catch (Exception e) {
            registrationStatus.setText("Error: " + e.getMessage());
        }
    }
}
