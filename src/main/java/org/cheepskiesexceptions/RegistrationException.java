package org.cheepskiesexceptions;

public class RegistrationException extends FlightSchedulingException {

    public RegistrationException (String message){
        super(message);
    }

    public RegistrationException (String message, Throwable cause){
        super(message);
    }
}
