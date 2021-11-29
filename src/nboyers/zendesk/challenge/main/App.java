
/*
 * @Author - Noah Boyers
 * Last Updated: 11/24/21
 */
package nboyers.zendesk.challenge.main;
import nboyers.zendesk.challenge.test.ZenRequest;
import java.util.Scanner;

/**
 * Main Class of the Ticket Viewer
 */
public class App extends ZenRequest {
    public static void main(String[] args) {
   establishConnection();
    }

    /**
     * Method called after the program starts to
     * as for user input
     */
     static void establishConnection() {
        String userChoice;
        Scanner sc = new Scanner(System.in);
        ZenRequest zR = new ZenRequest();

        do {
            System.out.println("Type 'menu' for options or 'quit' to exit");
            userChoice = sc.next();

            if (userChoice.equalsIgnoreCase("menu")) {
                System.out.println("Select view options");
                System.out.println("-------------------");
                System.out.println("* Press 1 to view all tickets");
                System.out.println("* Press 2 to view a ticket");
                System.out.println("* Type 'quit' to exit");
                userChoice = sc.next();
                sc.nextLine(); // Gets rid of the hidden "\n"

                switch (userChoice) {
                    case "1" -> zR.ticketController(-1);
                    case "2" -> {
                        System.out.println("What ticket ID would you like?");
                        if (sc.hasNextInt()) {
                            zR.ticketController(sc.nextInt());
                        } else {
                            System.out.println("ERROR: Not a number");
                            sc.nextLine(); // Gets rid of the hidden "\n"
                        }
                    }
                    case "quit" -> {
                        userChoice = "quit";
                        sc.close();
                    }
                    default -> System.out.println("Not a valid command");
                }
                System.out.println();
            } else {
                sc.nextLine();
            }
        } while (!userChoice.equalsIgnoreCase("quit"));
        sc.close();
        System.out.println("Thank you for using the viewer, Goodbye");
    }
}

