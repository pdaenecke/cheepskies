package org.cheepskies.common;

import org.cheepskies.ui.Customer;
import org.cheepskies.ui.Flight;
import java.util.ArrayList;

public class ValueObject {
    private String action;
    private Customer customer;
    private Flight flight;
   // private Credentials credentials;
    private ArrayList<Customer> customers;

//    public void setCredentials(Credentials credentials) {
//        this.credentials = credentials;
//    }

    public boolean operationResult;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ValueObject(){
        customer = new Customer();
        flight = new Flight();
        customers = new ArrayList<>();
//create customers?
    }
}
