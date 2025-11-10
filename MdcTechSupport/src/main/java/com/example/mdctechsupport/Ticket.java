package com.example.mdctechsupport;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashSet;

public class Ticket {

    private static final HashSet<Integer> usedIds = new HashSet<>();
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty issue;
    private final SimpleStringProperty priority;

    public Ticket(int id, String name, String issue, String priority) {

        if (id < 0)
            throw new IllegalArgumentException("Id can't have negative values");
        if (!Ticket.isIdAvailable(id))
            throw new IllegalArgumentException("Id is already used, try a different Id number");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name can't be blank or null");
        if(issue == null || issue.isBlank())
            throw new IllegalArgumentException("Issue can't be blank or null");
        if(!isPriorityValid(priority))
            throw new IllegalArgumentException("Priority has to be one of the following values (low, medium or high)");

        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name.trim());
        this.issue = new SimpleStringProperty(issue.trim());
        this.priority = new SimpleStringProperty(priority.toLowerCase().trim());
        usedIds.add(id);
    }

    public static boolean isIdAvailable(int id) {
        return !usedIds.contains(id);
    }

    public static boolean isPriorityValid(String priority) {
        if (priority == null)
            return false;

        priority = priority.toLowerCase();
        return (priority.equals("low") || priority.equals("medium") ||
                priority.equals("high"));
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getIssue() {
        return issue.get();
    }

    public SimpleStringProperty issueProperty() {
        return issue;
    }

    public String getPriority() {
        // Return nicely formatted version
        return priority.get().substring(0, 1).toUpperCase() + priority.get().substring(1);
    }

    public SimpleStringProperty priorityProperty() {
        return new SimpleStringProperty(
                priority.getValue().substring(0, 1).toUpperCase() +
                        priority.getValue().substring(1));
    }

    @Override
    public String toString() {
        return String.format("[#%d] %s - %s (%s)", id.getValue(), name, issue, getPriority());
    }
}
