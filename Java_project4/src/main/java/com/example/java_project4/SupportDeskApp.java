/*---------------------------------------------------
Name: Oscar Lopez, Enoc Bernal, Brenda Benitez
Student ID: Your Student IDs
COP 2805C - Java Programming 2
Fall 2025 - W 5:30 PM - 8:50 PM
Assignment # 3
Plagiarism Statement
I certify that this assignment is my own work and that I have not
copied in part or whole or otherwise plagiarized the work of other
students, persons, Generative Pre-trained Generators (GPTs) or any other AI tools.
I understand that students involved in academic dishonesty will face
disciplinary sanctions in accordance with the College's Student Rights
and Responsibilities Handbook (https://www.mdc.edu/rightsandresponsibilities)
01234567890123456789012345678901234567890123456789012345678901234567890123456789
----------------------------------------------------------*/


import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class SupportDeskApp {

    private static final Scanner input = new Scanner(System.in);
    private final SupportDesk supportDesk;

    public SupportDeskApp() {
        this.supportDesk = new SupportDesk();
    }

    public static void main(String[] args) {
        new SupportDeskApp().run();
    }

    // Pause console for user to read output
    private static void pauseConsole() {
        System.out.println("\nPress enter to go back to main menu...");
        while (input.hasNextLine()) {
            if (input.nextLine().isEmpty()) break;
        }
    }

    // Main loop
    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = promptMenuChoice();

            switch (choice) {
                case 1 -> addNewTicket();
                case 2 -> processNextTicket();
                case 3 -> viewAllActiveTickets();
                case 4 -> viewRecentlyResolved();
                case 5 -> reopenLastResolved();
                case 6 -> {
                    System.out.println("Thank you for using MDC Tech Support Ticket System!");
                    running = false;
                }
            }

            if (running) clearScreen();
        }
    }

    private void processNextTicket() {
        supportDesk.processNextTicket();
        pauseConsole();
    }

    private void viewAllActiveTickets() {
        supportDesk.viewAllActiveTickets();
        pauseConsole();
    }

    private void viewRecentlyResolved() {
        supportDesk.viewRecentResolved();
        pauseConsole();
    }

    private void reopenLastResolved() {
        supportDesk.reopenLastResolved();
        pauseConsole();
    }

    private void addNewTicket() {
        try {
            supportDesk.addTicket(createTicket());
            System.out.println("Ticket added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Input error: " + e.getMessage());
        }
        pauseConsole();
    }

    // Gather ticket details with validation
    private Ticket createTicket() {
        int id = promptInput(
                "Enter ticket ID: ",
                Integer::parseInt,
                n -> n > 0 && Ticket.isIdAvailable(n),
                "Invalid ID or already used."
        );

        String name = promptInput(
                "Enter requester name: ",
                s -> s,
                s -> !s.isBlank(),
                "Name can't be blank."
        );

        String issue = promptInput(
                "Enter issue: ",
                s -> s,
                s -> !s.isBlank(),
                "Issue can't be blank."
        );

        String priority = promptInput(
                "Enter priority (Low/Medium/High): ",
                s -> s,
                Ticket::isPriorityValid,
                "Priority must be Low, Medium, or High."
        );

        return new Ticket(id, name, issue, priority);
    }

    // Generic input prompt with conversion and validation
    private <T> T promptInput(String prompt, Function<String, T> converter,
                              Predicate<T> checker, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine();
            try {
                T value = converter.apply(line);
                if (checker.test(value)) return value;
                else System.out.println(errorMsg);
            } catch (Exception e) {
                System.out.println(errorMsg);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== Welcome to MDC Tech Support Ticket System =====");
        System.out.println("1. Add new support ticket");
        System.out.println("2. Process next ticket");
        System.out.println("3. View all active tickets");
        System.out.println("4. View recently resolved tickets");
        System.out.println("5. Reopen last resolved ticket");
        System.out.println("6. Exit");
    }

    private int promptMenuChoice() {
        while (true) {
            System.out.print("Enter your choice: ");
            String line = input.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= 6) return choice;
                else System.out.println("Choice must be 1–6.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number 1–6.");
            }
        }
    }

    // Simple clear screen
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
