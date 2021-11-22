/**
 * Class that handles all the API
 * Calls to Zendesk
 */
public class ZenRequest {

    /**
     * Method that the user calls to to get ticket info
     * from Zendesk API
     * @param userOptions - View Options of one or all tickets
     */
    public static void viewTickets(String userOptions){
        //FIXME Add options of viewing the ticket

        System.out.println(zenRequest());

    }

    /**
     * Method that returns the tickets as formated strings
     * @return - formatted Ticket as strings
     */
    private static String zenRequest(){
       return "STUBBED";
    }

}
