package org.cheepskies.ui;

import java.util.ArrayList;

public class Flight {
    private String departureLocation;
    private String departureTime;
    private String arrivalLocation;
    private String arrivalTime;
    private String flightDuration;
    private String arrivalDate;
    private String departureDate;
    private double price;
    private int flightId;





    public int getFlightId(){

        return flightId;
    }

    public void setFlightId(int flightId){
        this.flightId = flightId;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String departureDate) {
        this.arrivalDate = arrivalDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Flight(String departureLocation, String departureTime, String arrivalLocation,
                  String arrivalTime, String flightDuration, String departureDate, Double price) {
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.flightDuration = flightDuration;
        this.departureDate = departureDate;
        this.price = price;
    }

    public Flight() {
    }

    public Flight(String departureLocation, String departureTime, String arrivalLocation, String arrivalTime, String flightDuration, String arrivalDate, String departureDate, Double price, int flightId) {
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.flightDuration = flightDuration;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
        this.flightId = flightId;
    }

    public Flight(String arrivalLocation, String departureLocation, int flightId, Double price, String departureDate, String flightDuration) {
        this.arrivalLocation = arrivalLocation;
        this.departureLocation = departureLocation;
        this.flightId = flightId;
        this.price = price;
        this.departureDate = departureDate;
        this.flightDuration = flightDuration;
    }
//instead of override equals, may use WHERE clause dynamically based on the search criteria
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        Flight flight = (Flight) obj;
//
//        if(flight.flightId != 0 && flightId != flight.flightId) {
//            return false;
//        }
//        if(!flight.departureLocation.isEmpty() && !departureLocation.equals(flight.departureLocation)) {
//            return false;
//        }
//        if(!flight.arrivalLocation.isEmpty() && !arrivalLocation.equals(flight.arrivalLocation)) {
//            return false;
//        }
//        if(!flight.flightDuration.isEmpty() && !flightDuration.equals(flight.flightDuration)) {
//            return false;
//        }
//        if(!flight.departureDate.isEmpty() && !departureDate.equals(flight.departureDate)) {
//            return false;
//        }
//        if(this.price != 0 && this.price != flight.price) {
//            return false;
//        }
//        return true;
//    }
}
