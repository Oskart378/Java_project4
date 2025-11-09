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

import java.util.LinkedList;
import java.util.Stack;

public class SupportDesk {

    private final LinkedList<Ticket> activeTickets; // FIFO Queue
    private final Stack<Ticket> resolvedTickets;    // LIFO

    public SupportDesk() {
        activeTickets = new LinkedList<>();
        resolvedTickets = new Stack<>();
    }

    // Add ticket to the queue
    public void addTicket(Ticket t) {
        activeTickets.addLast(t);
    }

    // Process next ticket in queue and push to stack
    public void processNextTicket() {
        if (activeTickets.isEmpty()) {
            System.out.println("There are no more tickets to process.");
            return;
        }

        Ticket ticket = activeTickets.removeFirst();
        resolvedTickets.push(ticket);
        System.out.println("Processing next ticket...");
        System.out.println("Ticket resolved: " + ticket);
    }

    // Display up to 3 most recent resolved tickets
    public void viewRecentResolved() {
        int size = resolvedTickets.size();
        if (size == 0) {
            System.out.println("There are no resolved tickets at the moment.");
            return;
        }

        System.out.println("Recently resolved tickets:");
        int start = Math.max(0, size - 3); // last 3 tickets
        for (int i = size - 1; i >= start; i--) {
            System.out.print(resolvedTickets.get(i));
            if (i == size - 1) System.out.print("  <-- Most recently resolved");
            System.out.println();
        }
    }

    // Display all active tickets
    public void viewAllActiveTickets() {
        if (activeTickets.isEmpty()) {
            System.out.println("There are no active tickets at the moment.");
            return;
        }

        System.out.println("Active tickets:");
        for (Ticket t : activeTickets) {
            System.out.println(t);
        }
    }

    // Reopen last resolved ticket
    public void reopenLastResolved() {
        if (resolvedTickets.isEmpty()) {
            System.out.println("There are no resolved tickets to reopen.");
            return;
        }

        Ticket ticket = resolvedTickets.pop();
        activeTickets.addLast(ticket);
        System.out.println("Reopened ticket: " + ticket);
    }
}
