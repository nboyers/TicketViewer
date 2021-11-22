import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that handles all the API
 * Calls to Zendesk API
 */
public class ZenRequest {

    /**
     * Method that the user calls to to get ticket info
     * from Zendesk API
     */
    public static void viewTickets(){
        //FIXME Add options of viewing the ticket
        zenRequest();

    }

    /**
     * Method that connects to the Zendesk API
     */
    private static void zenRequest() {
      try {
          URL url = new URL("https://zccnboyers.zendesk.com/api/v2/tickets.json");
          HttpURLConnection http = (HttpURLConnection)url.openConnection();
          http.setRequestProperty("Accept", "application/json");
          http.setRequestProperty("Authorization", "Basic MDAtd2l0Y2hlcy1udW5jaW9zQGljbG91ZC5jb206d3VicWE4LXpld2d5aC1iZXRyZVA=");

          System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
          http.disconnect();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }

}
