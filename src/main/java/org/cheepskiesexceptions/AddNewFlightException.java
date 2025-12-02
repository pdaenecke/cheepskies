package org.cheepskiesexceptions;

public class AddNewFlightException extends Exception {
    public AddNewFlightException(String message) {
        super(message);
    }

    public AddNewFlightException(String message, Throwable cause) {
        super(message, cause);
    }
}
