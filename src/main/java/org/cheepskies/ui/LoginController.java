package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import javafx.stage.Stage;
import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseUtils;
import org.cheepskies.ui.Customer;

public class LoginController {
//fix to one customer obj
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

        Customer c = vo.getCustomer();

        try {
            Facade.process(vo);

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

        if (vo.operationResult) {
            loginStatus.setText("Login success.");
            openMainCloseLogin(c.getCustomerId());
        }
        else { loginStatus.setText("Login failed."); }

    }

    //Closes LoginApplication, opens MainApplication
    private void openMainCloseLogin(int customerId) {
        try {
            MainController mainController = MainApplication.openMain();
            mainController.setCurrentUser(customerId);
            ((Stage) login.getScene().getWindow()).close();
        } catch (IOException e) {
            loginStatus.setText("Error opening main page.");
        }
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