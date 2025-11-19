package org.cheepskies.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//Darrel
public class MainController /*implements Initializable*/ {

    @FXML
    private Button add;

    @FXML
    private Button remove;

    @FXML
    private Button search;

    @FXML
    private TextField flightId;

    @FXML
    private TableColumn<Flight, String> flightIdT;

    @FXML
    private TextField price;

    @FXML
    private TableColumn<Flight, String> priceT;

    @FXML
    private TextField departureDate;

    @FXML
    private TableColumn<Flight, String> departureDateT;

    @FXML
    private TextField departureTime;

    @FXML
    private TableColumn<Flight, String> departureTimeT;

    @FXML
    private TextField arrivalTime;

    @FXML
    private TableColumn<Flight, String> arrivalTimeT;

    @FXML
    private TextField arrivalDate;

    @FXML
    private TableColumn<Flight, String> arrivalDateT;

    @FXML
    private TextField departureLocation;

    @FXML
    private TableColumn<Flight, String> departureLocationT;

    @FXML
    private TextField arrivalLocation;

    @FXML
    private TableColumn<Flight, String> arrivalLocationT;

    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private TableView<Flight> flightsTableF;

    @FXML
    private Button logout;
}
