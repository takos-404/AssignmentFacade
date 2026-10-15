import facade.ClientBookingFacade;
import facade.AdminFacade;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        ClientBookingFacade clientFacade = new ClientBookingFacade();
        AdminFacade adminFacade = new AdminFacade();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== FLIGHT RESERVATION SYSTEM ===");
            System.out.println("1. Book Flight (Happy Path)");
            System.out.println("2. Book Flight (Fails at Step B: Overweight Baggage) -> Triggers A Rollback");
            System.out.println("3. Book Flight (Fails at Step C: Blocked Account) -> Triggers B & A Rollback");
            System.out.println("4. Admin: View Database State");
            System.out.println("5. Reset Database (Clear Text Files)");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println(clientFacade.bookFlight("A12", 15.0, "USER_99"));
                    break;
                case "2":
                    System.out.println(clientFacade.bookFlight("B05", 25.0, "USER_99")); // 25kg > 20kg
                    break;
                case "3":
                    System.out.println(clientFacade.bookFlight("C01", 10.0, "BLOCKED")); // BLOCKED account
                    break;
                case "4":
                    adminFacade.printSystemState();
                    break;
                case "5":
                    new File("A_reservations.txt").delete();
                    new File("B_tickets.txt").delete();
                    new File("C_loyalty.txt").delete();
                    new File("Chain_states.txt").delete();
                    System.out.println("Database reset.");
                    break;
                case "0":
                    System.exit(0);
            }
        }
    }
}