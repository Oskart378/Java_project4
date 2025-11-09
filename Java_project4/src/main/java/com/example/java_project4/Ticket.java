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

import java.util.HashSet;

public class Ticket {

    private static final HashSet<Integer> usedIds = new HashSet<>();
    private final int id;
    private final String name;
    private final String issue;
    private final String priority;

    public Ticket(int id, String name, String issue, String priority) {
        // Validate inputs
        if (id < 0)
            throw new IllegalArgumentException("ID can't be negative");
        if (!Ticket.isIdAvailable(id))
            throw new IllegalArgumentException("ID already used");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name can't be blank");
        if (issue == null || issue.isBlank())
            throw new IllegalArgumentException("Issue can't be blank");
        if (priority == null || !isPriorityValid(priority))
            throw new IllegalArgumentException("Priority must be Low, Medium, or High");

        this.id = id;
        this.name = name.trim();
        this.issue = issue.trim();
        this.priority = priority.toLowerCase().trim();
        usedIds.add(id); // mark ID as used
    }

    public static boolean isIdAvailable(int id) {
        return !usedIds.contains(id);
    }

    public static boolean isPriorityValid(String priority) {
        priority = priority.toLowerCase();
        return priority.equals("low") || priority.equals("medium") || priority.equals("high");
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIssue() { return issue; }

    // Return priority with first letter capitalized
    public String getPriority() {
        return priority.substring(0, 1).toUpperCase() + priority.substring(1);
    }

    @Override
    public String toString() {
        return String.format("[#%d] %s - %s (%s)", id, name, issue, getPriority());
    }
}
