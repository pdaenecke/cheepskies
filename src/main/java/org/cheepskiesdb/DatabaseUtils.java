package org.cheepskiesdb;

import org.cheepskies.common.ValueObject;
import org.cheepskies.ui.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            System.out.println(e);
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
            System.out.println(e);
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
            System.out.println(e);
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


}