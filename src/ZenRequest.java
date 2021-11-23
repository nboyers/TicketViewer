/*
*
* @author - Noah Boyers
* Last Updated: 11/23/21
 */


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

//TODO: Parse JSON DATA
// Format to be readable by user
// README file to tell user to make a directory for testing API / Basic credentials

/**
 * ZenRequests.java does all the heavy lifting for the
 * program by calling to the API and handling what
 * happens with that information to send back to the user
 */
public class ZenRequest {
    // Used a text file as a placeholder for credentials

    // Used a text file as a placeholder for credentials
    private final static String ZENDESK_RESOURCE_FILE = "/Users/laptop81/Desktop/ZendeskResources.txt";

    /**
     * Method that handles viewing of the tickets
     * @param userChoice - Option to see a single ticket or the first 25 tickets
     */
    public static void viewTickets(String userChoice){
        //FIXME Add options of viewing the ticket
        zenRequest();

    }

    /**
     * Connects to the Zendesk API
     * and tries to establish a connection
     */
    private static void zenRequest(){

// Establishes connection to Zendesk API
        try {
            URL url = new URL(loadURL());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Authorization", "Basic " + clientSecret());
            int responseCode = http.getResponseCode();

            if (responseCode != 200)
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            //TODO: Have connection to API, now just need to parse data


        } catch (IOException e) {
            e.printStackTrace();
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

}

