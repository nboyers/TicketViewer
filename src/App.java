/*
 *
 * @author - Noah Boyers
 * Last Updated: 11/24/21
 */

import java.util.Scanner;

/**
 * Main Class of the Ticket Viewer
 */
public class App  extends ZenRequest {
    public static void main(String[] args) {
        establishConnection();
    }

    /**
     * Method called after the program starts to
     * as for user input
     */
    private static void establishConnection() {
        String userChoice;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Type 'menu' to view options or 'quit' to exit");
            userChoice = sc.nextLine();

            if(userChoice.equalsIgnoreCase("menu")){
                System.out.println();
                System.out.println("Select view options");
                System.out.println("* Press 1 to view all tickets");
                System.out.println("* Press 2 to view a ticket");
                System.out.println("* Type 'quit' to exit");
                userChoice = sc.nextLine();

                if(userChoice.equalsIgnoreCase("1")
                        || userChoice.equalsIgnoreCase("2")) {
                    viewTickets(userChoice);
                } else {
                    System.out.println("Not a valid option: Try again.");
                }
            } else if (userChoice.equalsIgnoreCase("quit")) {
                sc.close(); //closes the scanner
            } else {
                System.out.println("Unknown Command: Try again.");
            }
        } while(!userChoice.equalsIgnoreCase("quit"));
        System.out.println("Thank you for using the viewer, Goodbye");
    }
}

