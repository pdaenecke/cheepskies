package org.cheepskies.bizlogic;

import org.cheepskies.common.ValueObject;
import org.cheepskies.ui.Customer;
import org.cheepskies.ui.Flight;
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
    //do i even need this??
//    public boolean searchFlightsAsCustomer(ValueObject vo) throws SQLException {
//
//
//    }

    public boolean adminAddFlight(ValueObject vo) throws AddNewFlightException {
        Flight flight = vo.getFlight();

        if (DatabaseUtils.flightAlreadyExists(flight.getFlightId())) {
            throw new AddNewFlightException("Flight already exists.");
        }
        else {
            return true;
        }
    }

}