package org.cheepskies.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.cheepskies.common.ValueObject;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TextField arrivalLocationTextBox;

    @FXML
    private TableColumn<Flight, String> arrivallocation;

    @FXML
    private Label arriveLocationLabel;

    @FXML
    private Label departDateLabel;

    @FXML
    private TextField departLocTextBox;

    @FXML
    private TextField departureDateTextBox;

    @FXML
    private Label departureLocLabel;

    @FXML
    private TableColumn<Flight, String> departuredate;

    @FXML
    private TableColumn<Flight, String> departurelocation;

    @FXML
    private Label flightDurLabel;

    @FXML
    private TextField flightDurTextBox;

    @FXML
    private Label flightIdLabel;

    @FXML
    private TextField flightIdTextBox;

    @FXML
    private TableColumn<Flight, String> flightduration;

    @FXML
    private TableColumn<Flight, Integer> flightid;

    @FXML
    private TableColumn<Flight, String> price;

    @FXML
    private Label priceLabel;

    @FXML
    private TextField priceTextBox;

    @FXML
    private TableView<Flight> tableView;

    @FXML
    private Button searchButton;

    @FXML
    void removeFlightFromCustomer(MouseEvent event) throws SQLException, ClassNotFoundException{



    }

    @FXML
    void returnToMainMenu(MouseEvent event) {

    }


    ValueObject vo = new ValueObject();
    ObservableList<Flight> list = FXCollections.observableArrayList(vo.getFlights());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Search starting... ");

        try {

            arrivallocation.setCellValueFactory(new PropertyValueFactory<Flight, String>("arrivalLocation"));
            departurelocation.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureLocation"));
            flightid.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("flightId"));
            flightduration.setCellValueFactory(new PropertyValueFactory<Flight, String>("flightDuration"));
            departuredate.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));
            price.setCellValueFactory(new PropertyValueFactory<Flight, String>("price"));
        }
        catch (Exception e) {
            System.out.println("Error while retrieving search...");
            }

        }

    }



