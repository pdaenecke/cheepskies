package org.cheepskies.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private ObservableList<Flight> allFlights;
    @FXML
    private TextField arrivalLocationTextBox;

    @FXML
    private TableColumn<Flight, String> arrivallocation;

    @FXML
    private Label arriveLocationLabel;

    @FXML
    private Button toMainMenuButton;

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
    private Button adminAddFlight;

    @FXML
    private Label adminOnlyLabel;
//button that displays warning message
    @FXML
    private Button adminRemoveFlightSt;

    @FXML
    private Button adminSearchAllFlights;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void removeFlightFromCustomer(MouseEvent event) throws SQLException, ClassNotFoundException{

    }

    @FXML
    void returnToMainMenu(MouseEvent event) {
        try {
            // Loading the new FXML file
            Parent newPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/gui/cheepskies/main-page.fxml")));
            // Getting the current stage
            Stage stage = (Stage) toMainMenuButton.getScene().getWindow();

            // Setting the new scene
            Scene scene = new Scene(newPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
            allFlights = FXCollections.observableArrayList();
            loadAllFlights();

        }
        catch (Exception e) {
            System.out.println("Error while retrieving search...");
            }

        }

    @FXML
    void addFlightClick(MouseEvent event) {

    }
//displays warning message, does not delete with this button
    @FXML
    void removeFlightClickSt(MouseEvent event) {

    }

//searching flights based on textboxes in UI
    @FXML
    void searchFlightsClick(MouseEvent event) {
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

            //Dynamically builds WHERE clause, if not empty pull text from fields to include in query. If empty where 1=1 (no filter aka. *)
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

    //displays all flights in flight table when opening admin page
    private void loadAllFlights() {
        allFlights.clear(); //Prevents record duplication
        String query = "SELECT * FROM flights";

        System.out.println("Loading all flights from database..."); //debugging stuff

        //Establish DB connection and queries
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery();) {

            int count = 0; //debugging

            //while next result is true, create flight object with fetched result
            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getString("departurelocation"),
                        rs.getString("departuretime"),
                        rs.getString("arrivallocation"),
                        rs.getString("arrivaltime"),
                        rs.getString("flightduration"),
                        rs.getString("departuredate"),
                        rs.getDouble("price")
                );
                flight.setFlightId(rs.getInt("flightid"));
                allFlights.add(flight); //add flights to observable list

                //debugging stuff
                count++;
                System.out.println("Loaded flight: " + flight.getFlightId() + " - " +
                        flight.getDepartureLocation() + " to " + flight.getArrivalLocation());
            }

            System.out.println("Total flights loaded: " + count); //debugging stuff
            tableView.setItems(allFlights);

        } catch (SQLException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
    }
    }



