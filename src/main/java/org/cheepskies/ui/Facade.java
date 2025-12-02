package org.cheepskies.ui;
import org.cheepskies.bizlogic.BizLogic;
import org.cheepskies.common.ValueObject;
import org.cheepskiesexceptions.*;
//preston

public class Facade {

    private static BizLogic bizlogic = new BizLogic();

    public static void process(ValueObject vo) {

        //try block contains the string action for our bizlogic functions
        try {
            switch (vo.getAction()) {
                case "login":
                    vo.operationResult = bizlogic.login(vo);
                    break;
                case "addFlight":
                    vo.operationResult = bizlogic.addFlightToCustomer(vo);
                    break;
                case "removeFlight":
                    vo.operationResult = bizlogic.removeFlightFromCustomer(vo);
                    break;
                case "register":
                //missing logic
                case "searchFlightCustomer":
                    vo.operationResult = bizlogic.searchFlightsAsCustomer(vo);
                    break;
            }
            // catch block contains the exceptions for our bizlogic functions
        } catch (LoginException e) {
            vo.operationResult = false;
            System.out.println("Login error: " + e.getMessage());
        } catch (RegistrationException e) {
            vo.operationResult = false;
            System.out.println("Registration error: " + e.getMessage());
        } catch (AddFlightRecordDuplicationException e) {
            vo.operationResult = false;
            System.out.println("Flight already added: " + e.getMessage());
        } catch (AddToFlightListException e) {
            vo.operationResult = false;
            System.out.println("Error adding flight: " + e.getMessage());
        } catch (RemoveFlightRecordException e) {
            vo.operationResult = false;
            System.out.println("Error removing flight: " + e.getMessage());
        } catch (FlightSchedulingException e) {
            vo.operationResult = false;
            System.out.println("Flight scheduling error: " + e.getMessage());
        } catch (Exception e) {
            vo.operationResult = false;
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


}
