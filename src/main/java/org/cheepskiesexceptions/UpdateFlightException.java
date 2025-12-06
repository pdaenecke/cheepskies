package org.cheepskiesexceptions;

public class UpdateFlightException extends Exception {

    public UpdateFlightException(String message) {
        super(message);
    }

    public UpdateFlightException(String message, Throwable cause) {
        super(message, cause);
    }

}
