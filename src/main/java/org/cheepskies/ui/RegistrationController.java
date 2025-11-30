package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    void accountCreation(MouseEvent event) {
        String first = firstName.getText();
        String last = lastName.getText();
        String middle = middleInitial.getText();
        String user = username.getText();
        String pass = password.getText();
        String mail = email.getText();

        // checks if any fields are empty
        if (first.isEmpty() || last.isEmpty() || middle.isEmpty() || user.isEmpty() || pass.isEmpty() || mail.isEmpty()) {
            registrationStatus.setText("One or more fields are empty.");
            return;
        }

        // checks if middle character is more than just an initial
        if (middle.length() > 1) {
            registrationStatus.setText("Middle initial is more than one character.");
            return;
        }

        // these two methods are imported DatabaseUtils to check if the username or email already exist, if they do registration is cancelled.
        if (DatabaseUtils.userScan(user)) {
            registrationStatus.setText("Username already exists.");
            return; // stops registration
        }

        // ^
        if (DatabaseUtils.emailScan(mail)) {
            registrationStatus.setText("Email already exists.");
            return; // stops registration
        }

        // empty id object to store id in next code block
        int id = 0;

        // sql queries
        String sqlCustomers = "INSERT INTO customers (first_name, middle_initial, last_name, email) VALUES (?, ?, ?, ?)";
        String sqlCredentials = "INSERT INTO credentials (customer_id, username, password) VALUES (?, ?, ?)";

        // connects to database using databaseConnect method
        try (Connection connection = DatabaseConnector.dbConnect()) {

            // prepares the customer query, and grabs the generated key
            try (PreparedStatement statement = connection.prepareStatement(sqlCustomers, PreparedStatement.RETURN_GENERATED_KEYS)) {
                // sets the query up, and executes it
                statement.setString(1, first);
                statement.setString(2, middle);
                statement.setString(3, last);
                statement.setString(4, mail);
                statement.executeUpdate();

                // result set of generates keys
                var keys = statement.getGeneratedKeys();

                // if a key exists, sets id object to its value
                if (keys.next()) {
                    id = keys.getInt(1);
                }
            }

            // same as above, preparing the sql statement for the credentials
            try (PreparedStatement statement = connection.prepareStatement(sqlCredentials)) {

                // sets the query up, including the foreign generated key from the customer query, and executes it
                statement.setInt(1, id);
                statement.setString(2, user);
                statement.setString(3, pass);
                statement.executeUpdate();

            }

            //inject SQL statement to pull login cred
            //try ()

            registrationStatus.setText("Registration complete. Press x to login.");
            System.out.print("Registration complete.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
