package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseUtils;
import org.cheepskies.ui.Customer;

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

        ValueObject vo = new ValueObject();
        vo.getCustomer().credentials(user, pass);
        vo.setAction("login");

        try {
            Facade.process(vo);

            Customer c = vo.getCustomer();

            System.out.println("=== Customer Info ===");
            System.out.println("Customer ID: " + c.getCustomerId());
            System.out.println("First Name: " + c.getFirstName());
            System.out.println("Middle Initial: " + c.getmI());
            System.out.println("Last Name: " + c.getLastName());
            System.out.println("Email: " + c.getEmail());
            System.out.println("Username: " + c.getUsername());
            System.out.println("Password: " + c.getPassword());
            System.out.println("=====================");

        } catch (Exception e) {
            System.out.println(e);
        }

        if (vo.operationResult) { loginStatus.setText("Login success."); }
        else { loginStatus.setText("Login failed."); }

        Customer c = vo.getCustomer();


        /* checks if either field is empty
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
*/
    }

    @FXML
    void registrationRedirect(MouseEvent event) throws IOException {
        try {
            RegistrationWindow.open();
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}