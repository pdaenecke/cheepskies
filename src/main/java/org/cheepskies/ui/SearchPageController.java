package org.cheepskies.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseConnector;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchPageController implements Initializable {

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

    @FXML
    void searchFlights(MouseEvent event) {
        try {
            // Get user input from text fields
            String flightId = flightIdTextBox.getText().trim();
            String departLoc = departLocTextBox.getText().trim();
            String arrivalLoc = arrivalLocationTextBox.getText().trim();
            String departDate = departureDateTextBox.getText().trim();
            String flightDur = flightDurTextBox.getText().trim();
            String priceStr = priceTextBox.getText().trim();

            // Build the SQL query dynamically with placeholders
            StringBuilder query = new StringBuilder("SELECT * FROM flights WHERE 1=1");
            List<String> parameters = new ArrayList<>();
// is this the same as equals I have already overridden
            if (!flightId.isEmpty()) {
                query.append(" AND flightId = ?");
                parameters.add(flightId);
            }
            if (!departLoc.isEmpty()) {
                query.append(" AND departureLocation = ?");
                parameters.add(departLoc);
            }
            if (!arrivalLoc.isEmpty()) {
                query.append(" AND arrivalLocation = ?");
                parameters.add(arrivalLoc);
            }
            if (!departDate.isEmpty()) {
                query.append(" AND departureDate = ?");
                parameters.add(departDate);
            }
            if (!flightDur.isEmpty()) {
                query.append(" AND flightDuration = ?");
                parameters.add(flightDur);
            }
            if (!priceStr.isEmpty()) {
                query.append(" AND price = ?");
                parameters.add(priceStr);
            }


            DatabaseConnector db = new DatabaseConnector();

            // Execute the query with parameters
            ResultSet rs = db.executePreparedQuery(query.toString(), parameters);

            // Create a list to hold the results
            ObservableList<Flight> flightList = FXCollections.observableArrayList();

            // Loop through results and create Flight objects
            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getString("arrivalLocation"),
                        rs.getString("departureLocation"),
                        rs.getInt("flightId"),
                        rs.getDouble("price"),
                        rs.getString("departureDate"),
                        rs.getString("flightDuration")
                );
                flightList.add(flight);
            }

            // Display results in the table
            tableView.setItems(flightList);

        } catch (Exception e) {
            e.printStackTrace();
            // Consider showing an error alert to the user
        }
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



