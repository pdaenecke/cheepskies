package org.cheepskies.ui;

import com.google.protobuf.Value;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cheepskies.common.ValueObject;

public class PasswordController {

    @FXML
    private TextField favoriteAnimal;

    @FXML
    private TextField passwordOutput;

    @FXML
    private TextField username;

    @FXML
    private Button validateButton;

    @FXML
    private Label recoveryStatus;

    @FXML
    void validateAnswer(MouseEvent event) {

        String animal = favoriteAnimal.getText();
        String user = username.getText();

        if (animal.isEmpty() || user.isEmpty()) {
            recoveryStatus.setText("Missing fields");
            return;
        }

        ValueObject vo = new ValueObject();
        Customer c = vo.getCustomer();

        vo.getCustomer().setUsername(user);
        vo.getCustomer().setAnswer(animal);

        vo.setAction("passwordRecovery");

        try {
            Facade.process(vo);

            if (vo.operationResult) {
                passwordOutput.setText(vo.getCustomer().getPassword());
                recoveryStatus.setText("Recovery successful");
            } else {
                recoveryStatus.setText("Recovery failed");
            }
        } catch (Exception e) {
            recoveryStatus.setText("Error: " + e.getMessage());
        }
    }
}
