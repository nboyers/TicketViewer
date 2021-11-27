/*
 *
 * @Author - Noah Boyers
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
        int ticketID;
        Scanner sc = new Scanner(System.in);
        ZenRequest zR = new ZenRequest();


        do {
            System.out.println("Select view options");
            System.out.println("-------------------");
            System.out.println("* Press 1 to view all tickets");
            System.out.println("* Press 2 to view a ticket");
            System.out.println("* Type 'quit' to exit");
            userChoice = sc.nextLine();

            switch (userChoice) {
                case "1" -> zR.viewTickets(-1);
                case "2" -> { //FIXME: Minor UI bug in which the menu displays twice when option 2 is shown
                    System.out.println("What ticket ID would you like?");
                    ticketID = sc.nextInt();
                    zR.viewTickets(ticketID);
                }
                case "quit" -> {
                    sc.close();
                    userChoice = "quit";
                }
                default -> System.out.println("Not a valid command");
            }
        }while (!userChoice.equalsIgnoreCase("quit")) ;
        sc.close();
        System.out.println("Thank you for using the viewer, Goodbye");
    }
}

