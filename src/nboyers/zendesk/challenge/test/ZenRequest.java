package nboyers.zendesk.challenge.test;
/*
 * @Author - Noah Boyers
 * Last Updated: 11/28/21
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;


/**
 * ZenRequests.java does all the heavy lifting for the
 * program by calling to the API and handling what
 * happens with that information to send back to the user
 */
public class ZenRequest {
    // Used a text file as a placeholder for credentials
    final static String ZENDESK_RESOURCE_FILE = "/Users/laptop81/Desktop/ZendeskResources.txt";
    static int jsonValue;
    static private String secret;
    static int count = 0;

    /**
     * Default Constructor
     */
    public ZenRequest() {
    }

    /**
     * Method that handles viewing of the tickets
     *
     * @param userChoice - Option to see a single ticket or the first 25 tickets
     */
    public void ticketController(int userChoice) {
        try {
            String jsonResults = readJsonFromUrl();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(jsonResults).getAsJsonObject();
            JsonArray results = json.getAsJsonArray("tickets");

            if (userChoice != -1) {
                if (isValuePresent(results, userChoice)) { // checks if id is in system
                    System.out.println(printJsonResults(results, getKeyValue()));
                } else {
                    System.out.println("No ticket with that ID is in the system.");
                }
            } else {
                System.out.println(printJsonResults(results, -1));
            }
        } catch (InputMismatchException e) {
            System.out.println("Not a valid response. Please enter the ticket number");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the
     *
     * @param results - JSON File
     * @param value   - Value of the key given
     */
    static String printJsonResults(JsonArray results, int value) {
        StringBuilder temp = new StringBuilder();
        int lastNum = results.size() - 1;
        try {
            if (value == -1) {
                if (count < results.size() - 1) { // Makes sure the position is remembered when paging through 25 at a time
                    for (int i = 0; i < 26; i++) {
                        JsonObject result = results.get(i).getAsJsonObject();
                        if (i >= lastNum) //Checks if the current ticket is close to the end, if so breaks
                            break;

                        temp.append("ID: ")
                                .append(result.get("id"))
                                .append(" Created at: ")
                                .append(result.get("created_at"))
                                .append(" Subject: ")
                                .append(result.get("subject"))
                                .append(" Status: ")
                                .append(result.get("status"))
                                .append("\n");
                        count++;
                    }
                }
                if (count > results.size() - 1) {
                    count = 0;
                }

            } else {
                JsonObject result = results.get(getKeyValue()).getAsJsonObject();
                temp.append("ID: ")
                        .append(result.get("id"))
                        .append(" Created at: ")
                        .append(result.get("created_at"))
                        .append(" Subject: ")
                        .append(result.get("subject"))
                        .append(" Status: ")
                        .append(result.get("status"));
            }
        } catch (JSONException e) {
            System.out.println("No JSON file was found");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("End of the tickets");
        }
        return temp.toString();
    }

    /**
     * finds if there is a value at a given Key
     *
     * @param results - JSON in question
     * @param jsonKey - Location of the
     * @return - Value is/not at
     */
    boolean isValuePresent(JsonArray results, int jsonKey) {
        for (int i = 0; i < results.size(); i++) {
            JsonObject result = results.get(i).getAsJsonObject();
            if (Objects.equals(result.get("id").toString(), String.valueOf(jsonKey))) {
                setKeyValue(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Setter for Value in a JSON file
     *
     * @param jsonValue - Value being passed
     */
    void setKeyValue(int jsonValue) {
        ZenRequest.jsonValue = jsonValue;
    }

    /**
     * Getter to retrieve the
     * value from the setValue() method
     *
     * @return - Value from the key
     */
    static int getKeyValue() {
        return jsonValue;
    }

    /**
     * Connects to the Zendesk API and returns the data
     * from the server to the client
     *
     * @return - JSON data as a string
     * @throws IOException   - Could not read from JSON
     * @throws JSONException - Could not find JSON
     */
    public static String readJsonFromUrl() throws IOException {
        //Declare Variables
        BufferedReader reader = null;
        try {
            loadURL();
            URL url = new URL(getSecret());
            int read;
            char[] chars = new char[1024];

            // Get the connection object
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Accept", "application/json");
            clientSecret();
            http.setRequestProperty("Authorization", "Basic " + getSecret());
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder buffer = new StringBuilder();

            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();

        } catch (IOException e) {
            System.out.println("Could not read from JSON");
        } catch (JSONException e) {
            System.out.println("Could not find JSON");
        } finally {
            if (reader != null)
                reader.close();
        }
        return "ERROR";
    }

    /**
     * Reads the first line of a file and returns it as a string
     * This is where it will get the URL for zenRequest Method
     *
     * @return - URL as a string on line 1 of the file
     */
    static boolean loadURL() {
        try {
            BufferedReader Buff = new BufferedReader(new FileReader(ZENDESK_RESOURCE_FILE));
            setSecret(Buff.readLine());
            return true;
        } catch (IOException e) {
            System.out.println("There was no URL found");
        }
        return false;
    }

    /**
     * Method that reads the config file
     * and returns the client auth
     *
     * @return - client pass as a Base64 encoding String
     */
    static boolean clientSecret() {
        try {
            BufferedReader Buff = new BufferedReader(new FileReader(ZENDESK_RESOURCE_FILE));
            Buff.readLine(); // Trashes the first line
            //Converts username & password to base64 encoding
            setSecret(Base64.getEncoder().encodeToString((Buff.readLine()).getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (IOException e) {
            System.out.println("Username and password was not in 'username:password' format");
        }
        return false;
    }

    /**
     * Getter to retrieves the secret
     * @return - Secret as a String
     */
    static String getSecret() {
        return secret;
    }

    /**
     * Method that sets the CLient secret
     * @param input - encoded client secret
     */
    static void setSecret(String input){
        secret = input;
    }

}

