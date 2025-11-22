package org.cheepskiesdb;

import com.google.protobuf.Value;
import org.cheepskies.common.ValueObject;
import org.cheepskies.ui.Customer;
import org.cheepskiesexceptions.AddToFlightListException;
import org.cheepskiesexceptions.FlightSchedulingException;
import org.cheepskiesexceptions.GetCustomerRecordException;
import org.cheepskiesexceptions.LoginException;
import org.cheepskiesexceptions.RemoveCustomerRecordException;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    /*
    public static ValueObject usernameScan(ValueObject vo) throws LoginException {
        if (vo.getCustomer() == null) {
            vo.operationResult = false;
            throw new LoginException("ValueObject must contain customer information");
        }

        String username = vo.getCredentials().getUsername();

        if (username == null || username.trim().isEmpty()) {
            vo.operationResult = false;
            throw new LoginException("Username cannot be empty");
        }

        String query = "SELECT COUNT(*) FROM credentials WHERE username = ?";

        try (Connection conn = DatabaseConnector.dbConnect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            // operationResult = true means username EXISTS (not available)
            // operationResult = false means username is AVAILABLE
            if (rs.next() && rs.getInt(1) > 0) {
                vo.operationResult = true;  // Username exists
            } else {
                vo.operationResult = false; // Username available
            }

            return vo;

        } catch (SQLException e) {
            System.out.println(e);
            vo.operationResult = false;
            throw new LoginException("Database error while scanning username: " + e.getMessage());
        }
    }
     */
    /*
    -Takes in ValueObject as a parameter with interchangeable flight information
    -Returns operationResult as true IF the operation is successful
    -Throws AddToFlightListException and FlightScheduling Exception
     */
    public static ValueObject addFlightToCustomer(ValueObject vo)
            throws AddToFlightListException, FlightSchedulingException {

        //Check if Customer and Flight are valid
        if (vo.getCustomer() == null || vo.getFlight() == null) {
            vo.operationResult = false;
            throw new AddToFlightListException("ValueObject must contain both customer and flight");
        }

        int customerId = vo.getCustomer().getCustomerId();
        int flightId = vo.getFlight().getFlightId();

        Connection conn = null;
        PreparedStatement checkFlightStmt = null;
        PreparedStatement checkCustomerStmt = null;
        PreparedStatement checkCapacityStmt = null;
        PreparedStatement checkDuplicateStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.dbConnect();
            conn.setAutoCommit(false);

            // Checks if flight exists
            checkFlightStmt = conn.prepareStatement(
                    "SELECT flightid FROM flights WHERE flightid = ?"
            );
            checkFlightStmt.setInt(1, flightId);
            rs = checkFlightStmt.executeQuery();

            //if flight does not exist
            if (!rs.next()) {
                vo.operationResult = false;
                throw new FlightSchedulingException("Flight with ID " + flightId + " does not exist");
            }
            rs.close();

            // Check if customer exists
            checkCustomerStmt = conn.prepareStatement(
                    "SELECT customer_id FROM customers WHERE customer_id = ?"
            );
            checkCustomerStmt.setInt(1, customerId);
            rs = checkCustomerStmt.executeQuery();

            //if customer does not exist
            if (!rs.next()) {
                vo.operationResult = false;
                throw new GetCustomerRecordException("Customer with ID " + customerId + " does not exist");
            }
            rs.close();

            // Check if customer already booked this flight
            checkDuplicateStmt = conn.prepareStatement(
                    "SELECT my_row_id FROM flight_customer WHERE flightid = ? AND customer_id = ?"
            );
            checkDuplicateStmt.setInt(1, flightId);
            checkDuplicateStmt.setInt(2, customerId);
            rs = checkDuplicateStmt.executeQuery();

            if (rs.next()) {
                vo.operationResult = false;
                throw new AddToFlightListException(
                        "Customer " + customerId + " has already booked flight " + flightId
                );
            }
            rs.close();

            // Check if flight has capacity
            checkCapacityStmt = conn.prepareStatement(
                    "SELECT capacity, " +
                            "(SELECT COUNT(*) FROM flight_customer WHERE flightid = ?) as current_bookings " +
                            "FROM flights WHERE flightid = ?"
            );
            checkCapacityStmt.setInt(1, flightId);
            checkCapacityStmt.setInt(2, flightId);
            rs = checkCapacityStmt.executeQuery();

            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                int currentBookings = rs.getInt("current_bookings");

                if (currentBookings >= capacity) {
                    vo.operationResult = false;
                    throw new FlightSchedulingException(
                            "Flight " + flightId + " is full (capacity: " + capacity + ")"
                    );
                }
            }
            rs.close();

            // Insert the booking
            insertStmt = conn.prepareStatement(
                    "INSERT INTO flight_customer (flightid, customer_id, capacity, is_max_capacity) " +
                            "VALUES (?, ?, " +
                            "(SELECT capacity FROM flights WHERE flightid = ?), " +
                            "(SELECT CASE WHEN (SELECT COUNT(*) + 1 FROM flight_customer WHERE flightid = ?) >= capacity " +
                            "THEN 1 ELSE 0 END FROM flights WHERE flightid = ?))"
            );
            insertStmt.setInt(1, flightId);
            insertStmt.setInt(2, customerId);
            insertStmt.setInt(3, flightId);
            insertStmt.setInt(4, flightId);
            insertStmt.setInt(5, flightId);

            int rowsAffected = insertStmt.executeUpdate();

            if (rowsAffected == 0) {
                vo.operationResult = false;
                throw new AddToFlightListException("Failed to add flight to customer, no rows affected");
            }

            conn.commit();
            vo.operationResult = true;
            System.out.println("Successfully added flight " + flightId + " to customer " + customerId);
            return vo;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            vo.operationResult = false;
            throw new AddToFlightListException("Database error while adding flight to customer: " + e.getMessage());
        } catch (FlightSchedulingException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            throw e;
        } finally {
            closeResources(rs, checkFlightStmt, checkCustomerStmt, checkCapacityStmt,
                    checkDuplicateStmt, insertStmt, (PreparedStatement) conn);
        }
    }

    public static ValueObject removeFlightFromCustomer(ValueObject vo)
            throws RemoveCustomerRecordException {

        if (vo.getCustomer() == null || vo.getFlight() == null) {
            vo.operationResult = false;
            throw new RemoveCustomerRecordException("ValueObject must contain both customer and flight");
        }

        int customerId = vo.getCustomer().getCustomerId();
        int flightId = vo.getFlight().getFlightId();

        Connection conn = null;
        PreparedStatement deleteStmt = null;

        try {
            conn = DatabaseConnector.dbConnect();

            deleteStmt = conn.prepareStatement(
                    "DELETE FROM flight_customer WHERE flightid = ? AND customer_id = ?"
            );
            deleteStmt.setInt(1, flightId);
            deleteStmt.setInt(2, customerId);

            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected == 0) {
                vo.operationResult = false;
                throw new RemoveCustomerRecordException(
                        "No booking found for customer " + customerId + " on flight " + flightId
                );
            }

            vo.operationResult = true;
            System.out.println("Successfully removed flight " + flightId + " from customer " + customerId);
            return vo;

        } catch (SQLException e) {
            vo.operationResult = false;
            throw new RemoveCustomerRecordException(
                    "Database error while removing flight from customer: " + e.getMessage()
            );
        } finally {
            closeResources((ResultSet) null, deleteStmt, (PreparedStatement) conn);
        }
    }

    private static void closeResources(ResultSet rs, PreparedStatement... statements) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        for (PreparedStatement stmt : statements) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private static void closeResources(ResultSet rs, Connection conn, PreparedStatement... statements) {
        closeResources(rs, statements);

        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
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
}
