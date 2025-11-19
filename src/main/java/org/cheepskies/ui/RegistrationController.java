package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cheepskiesdb.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private TextField username;

    @FXML
    void accountCreation(MouseEvent event) {
        String first = firstName.getText();
        String last = lastName.getText();
        String middle = middleInitial.getText();
        String user = username.getText();
        String pass = password.getText();
        String mail = email.getText();

        int id = 0;

        String sqlCustomers = "INSERT INTO customers (first_name, middle_initial, last_name, email) VALUES ( ?, ?, ?, ?)";
        String sqlCredentials = "INSERT INTO credentials (customer_id, username, password) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.dbConnect()) {

            try (PreparedStatement statement = connection.prepareStatement(sqlCustomers, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, first);
                statement.setString(2, middle);
                statement.setString(3, last);
                statement.setString(4, mail);
                statement.executeUpdate();

                var keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    id = keys.getInt(1);
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(sqlCredentials)) {
                statement.setInt(1, id);
                statement.setString(2, user);
                statement.setString(3, pass);
                statement.executeUpdate();

            }

            //inject SQL statement to pull login cred
            //try ()

            System.out.print("Registration complete.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
