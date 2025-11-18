package org.cheepskiesdb;

import org.cheepskies.common.ValueObject;
import org.cheepskiesexceptions.AddToFlightListException;
import org.cheepskiesexceptions.FlightSchedulingException;
import org.cheepskiesexceptions.GetCustomerRecordException;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    public boolean usernameScan(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.dbConnect(); PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static ValueObject addFlightToCustomer(ValueObject vo)
            throws AddToFlightListException, FlightSchedulingException {

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

            // Check if flight exists
            checkFlightStmt = conn.prepareStatement(
                    "SELECT flightid FROM flights WHERE flightid = ?"
            );
            checkFlightStmt.setInt(1, flightId);
            rs = checkFlightStmt.executeQuery();

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
}
