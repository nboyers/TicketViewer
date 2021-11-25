/*
*
* @author - Noah Boyers
* Last Updated: 11/24/21
 */
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

//TODO:
// Format to be readable by user (all tickets)
// and cycle through to find specific ticket


/**
 * ZenRequests.java does all the heavy lifting for the
 * program by calling to the API and handling what
 * happens with that information to send back to the user
 */
public class ZenRequest {
    // Used a text file as a placeholder for credentials
    private final static String ZENDESK_RESOURCE_FILE = "/Users/laptop81/Desktop/ZendeskResources.txt";
    /**
     * Method that handles viewing of the tickets
     * @param userChoice - Option to see a single ticket or the first 25 tickets
     */
    public static void viewTickets(String userChoice) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int ticketNumber = Integer.parseInt(userChoice) ;
        if(ticketNumber == 2) {
            System.out.println("Enter a ticket number");
            ticketNumber = scanner.nextInt();
            System.out.println(readJsonFromUrl(ticketNumber));
        } else {
            readJsonFromUrl(0);
        }
    }

    /**
     * Reads the first line of a file and returns it as a string
     * This is where it will get the URL for zenRequest Method
     * @return - URL as a string on line 1 of the file
     */
    private static String loadURL() throws IOException {
        BufferedReader Buff = new BufferedReader(new FileReader(ZENDESK_RESOURCE_FILE));
        return Buff.readLine();
    }

    /**
     * Method that reads the config file
     * and returns the client auth
     * @return - client pass as a Base64 encoding String
     */
    private static String clientSecret() throws IOException {
        BufferedReader Buff = new BufferedReader(new FileReader(ZENDESK_RESOURCE_FILE));
        Buff.readLine(); // Trashes the first line
        //Converts username & password to base64 encoding
        return Base64.getEncoder().encodeToString((Buff.readLine()).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Establishes connection to Zendesk API
     * and translates data
     * @return - JSON data a string
     */
    public static JSONObject readJsonFromUrl(int ticketID) throws IOException, JSONException {
        // Create URL objects
        URL url = new URL(loadURL());

        // Get the connection object
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        // Set the input stream allowed inputting data to the local machine
        http.setDoOutput(true);

        //Authentication
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Authorization", "Basic " + clientSecret());
        // Displays error code if not accepted
        int responseCode = http.getResponseCode();
        if (responseCode != 200)
            throw new RuntimeException("HttpResponseCode: " + responseCode);


        // Parses Data into a string with all the tickets
        if(ticketID == 0) {
            try (InputStream is = http.getInputStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONObject(jsonText);
            }
        } else {
            // Parses Data into a string with specific ticket
            try (InputStream is = http.getInputStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd, ticketID);
                return new JSONObject(jsonText);
            }
        }
    }

    /**
     * Reads all the data from the JSON file
     * @param rd -
     * @return - the JSON as a string
     * @throws IOException - if an I/O exception occurs
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        //Reads each character and turns it into a string
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Overloaded method that finds the ticket with a certain ID tag
     * @param rd - the reader of
     * @param ticketNumber - the Ticket number the user wants to pull up
     * @return - the Specific ticket
     * @throws IOException - if an I/O exception occurs
     */
    private static String readAll(Reader rd, int ticketNumber) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

}

