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
import javafx.stage.Stage;
import org.cheepskies.common.ValueObject;
import org.cheepskiesdb.DatabaseConnector;
import org.cheepskiesdb.DatabaseUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.cheepskiesdb.DatabaseConnector.dbConnect;

public class MainController implements Initializable {
    @FXML
    private Label planeEmoji1;

    @FXML
    private Label planeEmoji2;
    @FXML
    private Button add;

    @FXML
    private Button remove;

    @FXML
    private Button toAdminPageButton;

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
    private TextField arrivalLocationTextBox;

    @FXML
    private TextField arrivalLocationTextBox1;
    @FXML
    private Label arriveLocationLabel;

    @FXML
    private Label arriveLocationLabel1;

    @FXML
    private Label departDateLabel;

    @FXML
    private Label departDateLabel1;

    @FXML
    private TextField departLocTextBox;

    @FXML
    private TextField departLocTextBox1;
    @FXML
    private TextField departureDateTextBox;

    @FXML
    private TextField departureDateTextBox1;

    @FXML
    private Label departureLocLabel;

    @FXML
    private Label departureLocLabel1;
    @FXML
    private Label flightDurLabel;

    @FXML
    private Label flightDurLabel1;

    @FXML
    private TextField flightDurTextBox;

    @FXML
    private TextField flightDurTextBox1;
    @FXML
    private Label flightIdLabel;

    @FXML
    private Label flightIdLabel1;
    @FXML
    private TextField flightIdTextBox;

    @FXML
    private TextField flightIdTextBox1;
    @FXML
    private Label priceLabel;

    @FXML
    private Label priceLabel1;
    @FXML
    private TextField priceTextBox;

    @FXML
    private TextField priceTextBox1;
    @FXML
    private Label searchALLFLabel;
    @FXML
    private Button searchButton1;

    @FXML
    private Button searchButton2;

    @FXML
    private Label searchCustFLabel;

    @FXML
    private TableColumn<Flight, String> arrivalLocationT;

    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private TableView<Flight> flightsTableF;

    @FXML
    private Button logout;

    @FXML
    private TableColumn<Flight, String> flightIdF;


    @FXML
    private TableColumn<Flight, String> priceF;


    @FXML
    private TableColumn<Flight, String> departureDateF;


    @FXML
    private TableColumn<Flight, String> departureTimeF;


    @FXML
    private TableColumn<Flight, String> arrivalTimeF;


    @FXML
    private TableColumn<Flight, String> departureLocationF;


    @FXML
    private TableColumn<Flight, String> arrivalLocationF;

    @FXML
    private Button refresh;

    private ObservableList<Flight> allFlights;
    private ObservableList<Flight> userFlights;

    private int currentUserId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("MainController initialize started..."); //debugging stuff
        //set admin page button to not show / take up space by default, will be set to true by later admin verification
        toAdminPageButton.setVisible(false);
        toAdminPageButton.setManaged(false);

        try { //Calls the getter for each of these properties
            flightIdT.setCellValueFactory(new PropertyValueFactory<>("flightId"));
            priceT.setCellValueFactory(new PropertyValueFactory<>("price"));
            departureDateT.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
            departureTimeT.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
            arrivalTimeT.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
            departureLocationT.setCellValueFactory(new PropertyValueFactory<>("departureLocation"));
            arrivalLocationT.setCellValueFactory(new PropertyValueFactory<>("arrivalLocation"));

            //Calls the getter for each of these properties
            flightIdF.setCellValueFactory(new PropertyValueFactory<>("flightId"));
            priceF.setCellValueFactory(new PropertyValueFactory<>("price"));
            departureDateF.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
            departureTimeF.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
            arrivalTimeF.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
            departureLocationF.setCellValueFactory(new PropertyValueFactory<>("departureLocation"));
            arrivalLocationF.setCellValueFactory(new PropertyValueFactory<>("arrivalLocation"));

            //Real time updates between TableView and what the user sees
            allFlights = FXCollections.observableArrayList();
            userFlights = FXCollections.observableArrayList();


            System.out.println("About to load flights..."); //debugging stuff
            loadAllFlights();
            System.out.println("MainController initialize completed."); //debugging stuff

        } catch (Exception e) {
            System.out.println("Error during MainController initialization: " + e.getMessage()); //debugging stuff
        }
    }
//used to check if user is admin
    public void adminCheck(int custId) {
        //preparing query for prepared statement
        String query = "SELECT admin FROM customers WHERE customer_id = ?";
        //assigning current customer logged in to value object, also getting custID for verification of admin in prepared statement
        ValueObject vo = new ValueObject();
        Customer cust = new Customer();
        vo.setCustomer(cust);
        Facade.process(vo);

        try (Connection connection = dbConnect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1,custId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                if (rs.getInt("admin" ) == 1) {
                    System.out.println("Admin found with custId: " + custId);
                    toAdminPageButton.setVisible(true);
                    toAdminPageButton.setManaged(true);
                }
                else {
                    System.out.println("Customer with custId: " + custId + " is not admin.");

                }
            }
            else {
                System.out.println("No user found with ID: " + custId);
            }

    } catch (SQLException e) {
            System.out.println("Admin Check SQL Error: " + e.getMessage());

    } catch (Exception e) {
            System.out.println("Unexpected Admin Check Error: " + e.getMessage());
        }
    }

    //Sets current user ID after login, and while accessing MainApplication
    public void setCurrentUser(int userId) {
        System.out.println("DEBUG: setCurrentUser called with userId = " + userId);
        this.currentUserId = userId;
        loadUserFlights();
        adminCheck(userId);
        System.out.println("DEBUG: User flights loaded, count = " + userFlights.size());

    }


    //loads all available flights into flightTable
    private void loadAllFlights() {

        allFlights.clear(); //Prevents record duplication when refresh button is clicked
        String query = "SELECT * FROM flights";

        System.out.println("Loading all flights from database..."); //debugging stuff

        //Establish DB connection and queries
        try (Connection conn = dbConnect();
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
            flightsTable.setItems(allFlights);

        } catch (SQLException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
    }


    //Loads users personal flights into flightTableF
    private void loadUserFlights() {

        userFlights.clear(); //prevents flight duplication after refresh button is clicked.


        String query = "SELECT f.* FROM flights f " +
                "JOIN flight_customer fc ON f.flightid = fc.flightid " +
                "WHERE fc.customer_id = " + currentUserId;

        //Establish DB connection and queries
        try (Connection conn = dbConnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            //while next result set is true, populate users personal flight with queried information
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
                userFlights.add(flight); //add flights to observable list
            }
            flightsTableF.setItems(userFlights); //add to flightTable TableView

        } catch (SQLException e) {
            System.out.println("Error loading user flights: " + e.getMessage());
        }
    }


    @FXML
    //Refreshes tables based on click of refresh button
    public void refreshTables(MouseEvent event) {
        System.out.println("Refreshing tables...");
        loadAllFlights();
        if (currentUserId > 0) {
            loadUserFlights();
        }
        System.out.println("Tables refreshed.");
    }


    @FXML
    //adds flights to flightsTableF when add button is pressed
    public void addFlight(MouseEvent event) {
        Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem(); //whatever flight is selected

        if (selectedFlight == null) {
            System.out.println("No flight selected to add.");
            return;
        }
// DEBUG: Check what currentUserId is
        System.out.println("DEBUG: currentUserId = " + currentUserId);

        ValueObject vo = new ValueObject();
        vo.setAction("addFlight"); //switch case from facade
        vo.setFlight(selectedFlight);

        Customer customer = new Customer();
        customer.setCustomerId(currentUserId);
        vo.setCustomer(customer);

        try {
            Facade.process(vo);//facade process is run with set flight and customer

            /* boolean operation result of bizlogic.addFlightToCustomer */
            if (vo.operationResult) {
                userFlights.add(selectedFlight);
                System.err.println("Flight " + selectedFlight.getFlightId() + " added successfully.");
            } else {
                System.err.println("Failed to add flight.");
            }

        } catch (Exception e) {
            System.err.println("Error during add flight: " + e.getMessage());
        }
    }


    @FXML
    //removes flights from flightsTableF when remove button is pressed
    public void removeFlight(MouseEvent event) {
        Flight selectedFlight = flightsTableF.getSelectionModel().getSelectedItem(); //whatever flight is selected

        if (selectedFlight == null) {
            System.out.println("No flight selected to remove.");
            return;
        }

        ValueObject vo = new ValueObject();
        vo.setAction("removeFlight"); //switch case from facade
        vo.setFlight(selectedFlight);

        Customer customer = new Customer();
        customer.setCustomerId(currentUserId);
        vo.setCustomer(customer);

        try {
            Facade.process(vo); //facade process is run with set flight and customer

            //boolean operationResult of BizLogic.removeFlightFromCustomer
            if (vo.operationResult) {
                userFlights.remove(selectedFlight); //removes flight from observablelist
                System.out.println("Flight " + selectedFlight.getFlightId() + " removed successfully.");
            } else {
                System.err.println("Failed to remove flight.");
            }

        } catch (Exception e) {
            System.err.println("Error during remove flight: " + e.getMessage());
        }
    }

    @FXML
    public void logout(MouseEvent event) {
        try {
            // Open login page
            LoginApplication loginApp = new LoginApplication();
            Stage loginStage = new Stage();
            loginApp.start(loginStage);

            // Close current main page
            Stage currentStage = (Stage) logout.getScene().getWindow();
            currentStage.close();
            System.out.println("User logged out successfully.");

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
        }
    }

    //first table search button on main menu
    @FXML
    void searchFlights1(MouseEvent event) {
    // Getting user input from text fields
        String flightIdStr = flightIdTextBox.getText().trim();
        String departLoc = departLocTextBox.getText().trim();
        String arrivalLoc = arrivalLocationTextBox.getText().trim();
        String departDate = departureDateTextBox.getText().trim();
        String flightDur = flightDurTextBox.getText().trim();
        String priceStr = priceTextBox.getText().trim();

        ValueObject vo = new ValueObject();
        vo.setAction("searchFlight"); //switch case from facade

        try {

            //gets returned flights from db utils and puts it in arraylist "flights"
            ArrayList<Flight> flights = DatabaseUtils.searchAllFlights(flightIdStr, departLoc, arrivalLoc, departDate, flightDur, priceStr);
            //get flights and store in vo
            vo.setFlights(flights);
            //pass vo with flights to facade for exception handling
            Facade.process(vo);
            // Created list to display the results once facade is passable
            ObservableList<Flight> flightList = FXCollections.observableArrayList(flights);
            // Display results in the 1st table
            flightsTable.setItems(flightList);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
//second table search button on main menu
    @FXML
    void searchFlights2(MouseEvent event) {
//        try {
//            // Get user input from text fields
//            String flightId = flightIdTextBox1.getText().trim();
//            String departLoc = departLocTextBox1.getText().trim();
//            String arrivalLoc = arrivalLocationTextBox1.getText().trim();
//            String departDate = departureDateTextBox1.getText().trim();
//            String flightDur = flightDurTextBox1.getText().trim();
//            String priceStr = priceTextBox1.getText().trim();
//
//            // Building the SQL query dynamically with placeholders
//            StringBuilder query = new StringBuilder("SELECT f.* FROM flights f " +
//                    "JOIN flight_customer fc ON f.flightid = fc.flightid " +
//                    "WHERE fc.customer_id = " + currentUserId);
//
//            List<String> parameters = new ArrayList<>();
//
////            Flight flight = new Flight(arrivalLoc, departLoc, flightId, priceStr, departDate, flightDur); ???
//            if (!flightId.isEmpty()) {
//                query.append(" AND f.flightid = ?");
//                parameters.add(flightId);
//            }
//            if (!departLoc.isEmpty()) {
//                query.append(" AND f.departurelocation = ?");
//                parameters.add(departLoc);
//            }
//            if (!arrivalLoc.isEmpty()) {
//                query.append(" AND f.arrivallocation = ?");
//                parameters.add(arrivalLoc);
//            }
//            if (!departDate.isEmpty()) {
//                query.append(" AND f.departuredate = ?");
//                parameters.add(departDate);
//            }
//            if (!flightDur.isEmpty()) {
//                query.append(" AND f.flightduration = ?");
//                parameters.add(flightDur);
//            }
//            if (!priceStr.isEmpty()) {
//                query.append(" AND f.price = ?");
//                parameters.add(priceStr);
//            }

//            ValueObject vo = new ValueObject();
//            Customer customer = new Customer();
//
//            customer.setCustomerId(currentUserId);
//            vo.setCustomer(customer);
//
//            DatabaseConnector db = new DatabaseConnector();
//
//            // Execute the query with parameters
//            ResultSet rs = db.executePreparedQuery(query.toString(), parameters);
//
//            // Create a list to hold the results
//            ObservableList<Flight> flightList = FXCollections.observableArrayList();
//
//            // Loop through results and create Flight objects
//            while (rs.next()) {
//                Flight flight = new Flight(
//                        rs.getString("departurelocation"),
//                        rs.getString("departuretime"),
//                        rs.getString("arrivallocation"),
//                        rs.getString("arrivaltime"),
//                        rs.getString("flightduration"),
//                        rs.getString("departuredate"),
//                        rs.getDouble("price")
//                );
//                flight.setFlightId(rs.getInt("flightid"));
//                flightList.add(flight);
//
//            }
//
//            // Display results in the table
//            flightsTableF.setItems(flightList);
//
//        } catch (Exception e) {
//           e.printStackTrace();
        ValueObject vo = new ValueObject();
        vo.setAction("searchFlight"); //switch case from facade


        Customer customer = new Customer();
        customer.setCustomerId(currentUserId);
        vo.setCustomer(customer);
        loadUserFlights();

        String flightIdStr = flightIdTextBox1.getText().trim();
        String departLoc = departLocTextBox1.getText().trim();
        String arrivalLoc = arrivalLocationTextBox1.getText().trim();
        String departDate = departureDateTextBox1.getText().trim();
        String flightDur = flightDurTextBox1.getText().trim();
        String priceStr = priceTextBox1.getText().trim();

        vo.setAction("searchFlight"); //switch case from facade

        try {

            //gets returned flights from db utils and puts it in arraylist "flights"
            ArrayList<Flight> flights = DatabaseUtils.searchCustomerFlights(flightIdStr, departLoc, arrivalLoc, departDate, flightDur, priceStr, customer.getCustomerId());
            //get flights and store in vo
            vo.setFlights(flights);
            //pass vo with flights to facade for exception handling
            Facade.process(vo);
            // Created list to display the results once facade is passable
            ObservableList<Flight> flightList = FXCollections.observableArrayList(flights);
            // Display results in the 1st table
            flightsTableF.setItems(flightList);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        }
@FXML
void goToAdminPageClick(MouseEvent event) {

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/gui/cheepskies/admin-page.fxml"));
        Parent newPage = loader.load();

        // Get the admin controller and pass the current user ID
        AdminController adminController = loader.getController();
        adminController.setCurrentUserId(currentUserId); // Pass the user ID

        Stage stage = (Stage) toAdminPageButton.getScene().getWindow();
        // Setting the new scene
        Scene scene = new Scene(newPage);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
}}

