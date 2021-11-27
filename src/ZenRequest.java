/*
*
* @author - Noah Boyers
* Last Updated: 11/27/21
 */
import java.io.IOException;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Objects;
import com.google.gson.JsonArray;;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;


//TODO:
// Create Unit Testing

/**
 * ZenRequests.java does all the heavy lifting for the
 * program by calling to the API and handling what
 * happens with that information to send back to the user
 */
public class ZenRequest {
    // Used a text file as a placeholder for credentials
    private final static String ZENDESK_RESOURCE_FILE = "/Users/laptop81/Desktop/ZendeskResources.txt";
    private static int jsonValue;


public ZenRequest(){}

    /**
     * Method that handles viewing of the tickets
     * @param userChoice - Option to see a single ticket or the first 25 tickets
     */
    public void viewTickets(int userChoice) {
        try {
            String jsonResults = readJsonFromUrl();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(jsonResults).getAsJsonObject();
            JsonArray results = json.getAsJsonArray("tickets");

            if(userChoice != -1){ // Does NOT equal -1

                if(isValuePresent(results, userChoice)){ // checks if id is in system
                    printJsonResults(results, getKeyValue());
                } else {
                    System.out.println("No ticket with that ID is in the system.");
                }
            } else {
                printJsonResults(results,-1);
            }
        } catch (InputMismatchException e){
            System.out.println("Not a valid response. Please enter the ticket number");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the
     * @param results - JSON File
     * @param value - Value of the key given
     */
    private static void printJsonResults(JsonArray results, int value){
        try {
            if (value == -1){
                for (int i = 0; i < results.size() -1; i++) {
                    JsonObject result = results.get(i).getAsJsonObject();

                    System.out.println("ID: " + result.get("id") +
                            " Created at: " + result.get("created_at") +
                            " Subject: " + result.get("subject")+
                            " Status: " + result.get("status"));
                }

            } else {
                JsonObject result = results.get(getKeyValue()).getAsJsonObject();
                System.out.println("ID: " + result.get("id") +
                        " Created at: " + result.get("created_at") +
                        " Subject: " + result.get("subject"));
            }
        }catch (JSONException e){
            System.out.println("No JSON file was found");
        }
    }

    /**
     * finds if there is a value at a given Key
     * @param results - JSON in question
     * @param jsonKey - Location of the
     * @return - Value is/not at
     */
    private boolean isValuePresent(JsonArray results, int jsonKey){
        for(int i = 0; i < results.size(); i++){
            JsonObject result = results.get(i).getAsJsonObject();
            if(Objects.equals(result.get("id").toString(), String.valueOf(jsonKey))){
                setKeyValue(i);
                return true;
            }
        }
        return false;
}

    /**
     * Setter for Value in a JSON file
     * @param jsonValue - Value being passed
     */
    private void setKeyValue(int jsonValue){
      ZenRequest.jsonValue = jsonValue;
}

    /**
     * Getter to retrieve the
     * value from the setValue() method
     * @return - Value from the key
     */
    public static int getKeyValue(){
        return jsonValue;
}

// DONE
    /**
     * Connects to the Zendesk API and returns the data
     * from the server to the client
     * @return - JSON data as a string
     * @throws IOException - Could not read from JSON
     * @throws JSONException - Could not find JSON
     */
    private static String readJsonFromUrl() throws IOException, JSONException {
        //Declare Variables
        BufferedReader reader = null;
        try {
            URL url = new URL(loadURL());
            int read;
            char[] chars = new char[1024];

            // Get the connection object
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Authorization", "Basic " + clientSecret());
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder buffer = new StringBuilder();

            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
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

