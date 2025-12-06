package org.cheepskies.bizlogic;

import com.google.protobuf.Value;
import javafx.scene.chart.PieChart;
import org.cheepskies.common.ValueObject;
import org.cheepskies.ui.Customer;
import org.cheepskies.ui.Flight;
import org.cheepskies.ui.MainController;
import org.cheepskiesdb.DatabaseUtils;
import org.cheepskiesexceptions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BizLogic {

    public boolean login(ValueObject vo) throws Exception {

        Customer c = vo.getCustomer();

        if (!DatabaseUtils.usernameScan(c)) {
            vo.setAction("login_failed");
            return false;
        }

        if (!DatabaseUtils.loginValidation(c)) {
            vo.setAction("login_failed");
            return false;
        }

        DatabaseUtils.login(vo);
        vo.setAction("login_success");

        return true;
    }

    //Adds flights to userFlights through DatabaseUtils
    public boolean addFlightToCustomer(ValueObject vo) throws AddFlightRecordDuplicationException, AddToFlightListException {
        Flight flight = vo.getFlight();
        Customer customer = vo.getCustomer();

        // Check if customer already has this flight
        if (DatabaseUtils.customerHasFlight(customer.getCustomerId(), flight.getFlightId())) {
            throw new AddFlightRecordDuplicationException("Flight already exists in customer's list.");
        }

        // Check if flight has capacity
        if (!DatabaseUtils.flightHasCapacity(flight.getFlightId())) {
            throw new AddToFlightListException("Flight is at full capacity.");
        }

        // Add flight to customer and update capacity
        boolean success = DatabaseUtils.addFlightToCustomer(customer.getCustomerId(), flight.getFlightId());

        //if addFlightToCustomer not true, throw exception
        if (!success) {
            throw new AddToFlightListException("Failed to add flight to customer.");
        }

        return true;
    }

    //Removes flight from customer through DatabaseUtils
    public boolean removeFlightFromCustomer(ValueObject vo) throws RemoveFlightRecordException {
        Flight flight = vo.getFlight();
        Customer customer = vo.getCustomer();

        // Remove flight from customer and update capacity
        boolean success = DatabaseUtils.removeFlightFromCustomer(customer.getCustomerId(), flight.getFlightId());

        //if removeFlightFromCustomer not true throw exception
        if (!success) {
            throw new RemoveFlightRecordException("Failed to remove flight from customer.");
        }

        return true;
    }
//generic all search button functionality
    public boolean searchFlights(ValueObject vo) throws SQLException {
        Flight flight = vo.getFlight();
        Customer customer;
        customer = vo.getCustomer();

        if(DatabaseUtils.searchAllFlights(flight.getStrFlightId(), flight.getDepartureLocation(), flight.getArrivalLocation(), flight.getDepartureDate(), flight.getFlightDuration(), flight.getStrPrice()) == null) {
            throw new SQLException("Failed to retrieve flights from search.");
        }
        if(DatabaseUtils.searchCustomerFlights(flight.getStrFlightId(), flight.getDepartureLocation(), flight.getArrivalLocation(), flight.getDepartureDate(), flight.getFlightDuration(), flight.getStrPrice(), customer.getCustomerId()) == null) {
            throw new SQLException("Failed to retrieve flights from search.");
        }
        //if does not throw SQLException, result set is not empty and can proceed.
        return true;

    }

    public boolean adminAddFlight(ValueObject vo) throws AddNewFlightException, SQLException {

        Flight flight = vo.getFlight();

       boolean ok = DatabaseUtils.addFlight(flight);
       if (ok) {

           vo.setAction("add_success");
           vo.operationResult = true;
           System.out.println("Flight added successfully with ID: " + flight.getFlightId());
           return true;

       } else {
           throw new AddNewFlightException("Flight addition failed.");
       }
    }

    public boolean updateFlight(ValueObject vo) throws UpdateFlightException {
        return true;
    }

    public boolean register(ValueObject vo) throws RegistrationException {
        Customer c = vo.getCustomer();


        if (DatabaseUtils.userScan(c.getUsername())) {
            throw new RegistrationException("Username already exists.");
        }

        if (DatabaseUtils.emailScan(c.getEmail())) {
            throw new RegistrationException("Email already exists.");
        }

        int id = DatabaseUtils.insertCustomer(c);
        if (id == -1) {
            throw new RegistrationException("Unable to save customer.");
        }

        boolean ok = DatabaseUtils.insertCredentials(id, c.getUsername(), c.getPassword(), c.getAnswer());
        if (!ok) {
            throw new RegistrationException("Unable to save credentials.");
        }

        return true;
    }

    public boolean recoverPassword(ValueObject vo) throws RecoveryQuestionException {
        Customer c = vo.getCustomer();

        boolean ok = DatabaseUtils.recoveryScan(c);

        if (ok) {
            vo.setAction("recovery_success");
            vo.operationResult = true;
            return true;
        } else {
            throw new RecoveryQuestionException("Recovery question verification failed.");
        }
    }
}