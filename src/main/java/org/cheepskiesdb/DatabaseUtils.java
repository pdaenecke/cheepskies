package org.cheepskiesdb;

import org.cheepskies.common.ValueObject;
import org.cheepskies.ui.Customer;
import org.cheepskies.ui.Flight;
import org.cheepskiesexceptions.RecoveryQuestionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    /*
    -takes in customerId and flightId as parameters
    -remove flight from customer via flight_customer table
    -boolean return type so it can be set to a ValueObject operationResult
     */
    public static boolean removeFlightFromCustomer(int customerId, int flightId) {
        String deleteQuery = "DELETE FROM flight_customer WHERE flightid = ? AND customer_id = ?";

        //establish db connection
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(deleteQuery)) {

            //prepared statement values for ?'s
            statement.setInt(1, flightId);
            statement.setInt(2, customerId);

            int rowsAffected = statement.executeUpdate(); //rowsAffected = number of updated prepared statements
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error removing flight from customer: " + e.getMessage());
            return false;
        }
    }

    /*
    -takes in customerId and flightId as parameters
    -adds flight to customer via flight_customer table
    -boolean return type so it can be set to a ValueObject operationResult
     */
    public static boolean addFlightToCustomer(int customerId, int flightId) {
        String insertQuery = "INSERT INTO flight_customer (flightid, customer_id, capacity, is_max_capacity) VALUES (?, ?, ?, ?)";

        //establish db connection
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(insertQuery)) {

            //prepared statement values for ?'s
            statement.setInt(1, flightId);
            statement.setInt(2, customerId);
            statement.setInt(3, 1);
            statement.setBoolean(4, false);

            int rowsAffected = statement.executeUpdate(); //rowsAffected = number of updated prepared statements
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error adding flight to customer: " + e.getMessage());
            return false;
        }
    }

    public static int insertCustomer(Customer c) {
        String sql = "INSERT INTO customers (first_name, middle_initial, last_name, email, admin) VALUES (?, ?, ?, ?, false)";

        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getmI());
            stmt.setString(3, c.getLastName());
            stmt.setString(4, c.getEmail());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
            return -1;

        } catch (SQLException e) {
            System.out.println("Insert customer failed: " + e.getMessage());
            return -1;
        }
    }

    public static boolean insertCredentials(int id, String username, String password, String answer) {
        String sql = "INSERT INTO credentials (customer_id, username, password, security_answer) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, answer);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Insert credentials failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean emailScan(String email) {
        String query = "SELECT 1 FROM customers WHERE email = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, email);

            // this creates an object of the resulting SQL query table
            ResultSet rs = statement.executeQuery();

            // SQL queries set the cursor the row BEFORE resulting table, rs.next() moves the cursor to next row
            // return rs.next(); says, does the first row have a value? If it does, return true.
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    // usernameScan functionality only for usage in registration
    public static boolean userScan(String user) {
        String query = "SELECT 1 FROM credentials WHERE username = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, user);

            // this creates an object of the resulting SQL query table
            ResultSet rs = statement.executeQuery();

            // SQL queries set the cursor the row BEFORE resulting table, rs.next() moves the cursor to next row
            // return rs.next(); says, does the first row have a value? If it does, return true.
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean recoveryScan(Customer customer) {
        String query = "SELECT security_answer, password FROM credentials WHERE username = ?";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, customer.getUsername());

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String answer = rs.getString("security_answer");
            String password = rs.getString("password");

            if (answer.equals(customer.getAnswer())) {
                customer.setPassword(password);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error recovering password: " + e.getMessage());
            return false;
        }
    }

    public static boolean usernameScan(Customer customer) {
        String query = "SELECT 1 FROM credentials WHERE username = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, customer.getUsername());

            // this creates an object of the resulting SQL query table
            ResultSet rs = statement.executeQuery();

            // SQL queries set the cursor the row BEFORE resulting table, rs.next() moves the cursor to next row
            // return rs.next(); says, does the first row have a value? If it does, return true.
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error scanning for username: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginValidation(Customer customer) {
        String query = "SELECT password FROM credentials WHERE username = ?";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, customer.getUsername());

            // this creates an object of the resulting SQL query table
            ResultSet rs = statement.executeQuery();

            // SQL queries set the cursor the row BEFORE resulting table, rs.next() moves the cursor to next row
            // this is different, if !rs.next specifies, if the next row does NOT exist, execute if statement
            // in that instance it will false
            if (!rs.next()) {
                return false;
            }

            // looks at the table, which is only one row as username is a unique key
            // it then grabs the String value of the password table

            String pass = rs.getString("password");

            // if the password in the table is equal to the password parameter, return true
            return pass.equals(customer.getPassword());

        } catch (SQLException e) {
            System.out.println("Error validating login:" + e.getMessage());
            return false;
        }

    }

    public static void login(ValueObject vo) {
        Customer c = vo.getCustomer();

        String query = "SELECT cr.customer_id, c.first_name, c.middle_initial, c.last_name, c.email " +
                "FROM credentials cr " +
                "JOIN customers c ON cr.customer_id = c.customer_id " +
                "WHERE cr.username = ?";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, c.getUsername());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                c.setCustomerId(rs.getInt("customer_id"));
                c.setEmail(rs.getString("email"));
                c.setFirstName(rs.getString("first_name"));
                c.setmI(rs.getString("middle_initial"));
                c.setLastName(rs.getString("last_name"));
            }

        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    /*
        -checks if the set flight has capacity
        -takes in flightId as a parameter
        -boolean return type so it can be set as boolean operationResult within a ValueObject
     */
    public static boolean flightHasCapacity(int flightId) {
        String query = "SELECT capacity, is_max_capacity FROM flight_customer WHERE flightid = ?";

        //establish connection
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            //prepared statement setting
            statement.setInt(1, flightId);
            ResultSet rs = statement.executeQuery();


            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                boolean isMaxCapacity = rs.getBoolean("is_max_capacity");

                // If is_max_capacity is true, flight is full
                return !isMaxCapacity && capacity > 0;
            } else {
                // If no record exists, assume capacity is available
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error checking flight capacity: " + e.getMessage());
        }
        return false;
    }

    /*
        -checks that customer has flight
        -takes in flightId and customerId as a parameter
        -boolean return type so it can be set as boolean operationResult within a ValueObject
     */
    public static boolean customerHasFlight(int customerId, int flightId) {
        String query = "SELECT COUNT(*) FROM flight_customer WHERE customer_id = ? AND flightid = ?";

        //establish db connection and prepared statement
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            //prepared statement parameter setting
            statement.setInt(1, customerId);
            statement.setInt(2, flightId);
            ResultSet rs = statement.executeQuery();


            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking if customer has flight: " + e.getMessage());
        }
        return false;
    }
    //checks if admin is adding duplicate flight, flights can have same exact everything EXCEPT flightid (meaning, 2 flights going to same place)
    public static boolean flightAlreadyExists(int flightId) {

        String query = "SELECT * FROM flight WHERE flightid = ?";

        //establish db connection and prepared statement
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            //prepared statement parameter setting
            statement.setInt(1, flightId);
            ResultSet rs = statement.executeQuery();


            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking if flight exists: " + e.getMessage());
        }
        return false;

    }

    public static ArrayList<Flight> searchAllFlights(String flightIdStr, String departLoc, String arrivalLoc, String departDate, String flightDur, String priceStr) {
        ArrayList<Flight> flightsReturn = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM flights WHERE 1=1");
//dynamically building query to check for empty textboxes. Shows all flights if all are empty since no appending to statement happens.

        //using incremental index incase not all textboxes have input (index starts at 1, then ends at value = to number of inputs)
        int index = 1;
//establishing db connection, using connection to create empty prepared statement. (.toString() from StringBuilder(dynamic String))

            //index using post-increment so it starts at 1, then increments to 2 for next usage and so on...
            if (!flightIdStr.isEmpty()) {
                query.append(" AND flightid = ?");
            }
            if (!departLoc.isEmpty()) {
                query.append(" AND departurelocation = ?");
            }
            if (!arrivalLoc.isEmpty()) {
                query.append(" AND arrivallocation = ?");

            }
            if (!departDate.isEmpty()) {
                query.append(" AND departuredate = ?");

            }
            if (!flightDur.isEmpty()) {
                query.append(" AND flightduration = ?");

            }
            if (!priceStr.isEmpty()) {
                query.append(" AND price = ?");

            }
            //need to set up prepared statement after building query above
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query.toString())) {
            //index using post-increment so it starts at 1, then increments to 2 for next usage and so on...
            if (!flightIdStr.isEmpty()) {
                statement.setString(index++, flightIdStr);
            }
            if (!departLoc.isEmpty()) {
                statement.setString(index++, departLoc);
            }
            if (!arrivalLoc.isEmpty()) {
                statement.setString(index++, arrivalLoc);

            }
            if (!departDate.isEmpty()) {
                statement.setString(index++, departDate);

            }
            if (!flightDur.isEmpty()) {
                statement.setString(index++, flightDur);

            }
            if (!priceStr.isEmpty()) {
                statement.setString(index++, priceStr);
            }

//executes query, temp stores in result set to view table of data
            ResultSet rs = statement.executeQuery();

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
                flightsReturn.add(flight);
            }
            return flightsReturn;
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return flightsReturn;
    }

    public static ArrayList<Flight> searchCustomerFlights(String flightIdStr, String departLoc, String arrivalLoc, String departDate, String flightDur, String priceStr, int customerId) {
        ArrayList<Flight> flightsReturn = new ArrayList<>();
        //query specific per user, shows only that customer's booked flights
        StringBuilder query = new StringBuilder("SELECT f.* FROM flights f " +
                "JOIN flight_customer fc ON f.flightid = fc.flightid " +
                "WHERE fc.customer_id = ?");


//dynamically building query to check for empty textboxes. Shows all flights if all are empty since no appending to statement happens.
        //index using post-increment so it starts at 1, then increments to 2 for next usage and so on...
        if (!flightIdStr.isEmpty()) {
            query.append(" AND f.flightid = ?");
        }
        if (!departLoc.isEmpty()) {
            query.append(" AND f.departurelocation = ?");
        }
        if (!arrivalLoc.isEmpty()) {
            query.append(" AND f.arrivallocation = ?");

        }
        if (!departDate.isEmpty()) {
            query.append(" AND f.departuredate = ?");

        }
        if (!flightDur.isEmpty()) {
            query.append(" AND f.flightduration = ?");

        }
        if (!priceStr.isEmpty()) {
            query.append(" AND f.price = ?");

        }
        //establishing db connection, using connection to create empty prepared statement. (.toString() from StringBuilder(dynamic String))
        //need to set up prepared statement after building query above
        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query.toString())) {
            //index using post-increment so it starts at 1, then increments to 2 for next usage and so on...
            //using incremental index incase not all textboxes have input (index starts at 1, then ends at value = to number of inputs)
            int index = 1;
            statement.setInt(index++, customerId);
            if (!flightIdStr.isEmpty()) {
                statement.setString(index++, flightIdStr);
            }
            if (!departLoc.isEmpty()) {
                statement.setString(index++, departLoc);
            }
            if (!arrivalLoc.isEmpty()) {
                statement.setString(index++, arrivalLoc);

            }
            if (!departDate.isEmpty()) {
                statement.setString(index++, departDate);

            }
            if (!flightDur.isEmpty()) {
                statement.setString(index++, flightDur);

            }
            if (!priceStr.isEmpty()) {
                statement.setString(index++, priceStr);
            }

//executes query, temp stores in result set to view table of data
            ResultSet rs = statement.executeQuery();

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
                // added seperately using setFlightId
                flight.setFlightId(rs.getInt("flightid"));
                //adds completed flight to list
                flightsReturn.add(flight);
            }
            return flightsReturn;
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return flightsReturn;
    }

    public static boolean addFlight(Flight flight) throws SQLException {

        String query = "INSERT INTO flights (departurelocation, departuretime, arrivallocation, arrivaltime, flightduration, departuredate, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, flight.getDepartureLocation());
            stmt.setString(2, flight.getDepartureTime());
            stmt.setString(3, flight.getArrivalLocation());
            stmt.setString(4, flight.getArrivalTime());
            stmt.setString(5, flight.getFlightDuration());
            stmt.setString(6, flight.getDepartureDate());
            stmt.setDouble(7, flight.getPrice());

            int rows = stmt.executeUpdate();
            if (rows == 0) return false;


            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    flight.setFlightId(generatedId);
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error inserting flight: " + e.getMessage());
            return false;
        }
    }
}