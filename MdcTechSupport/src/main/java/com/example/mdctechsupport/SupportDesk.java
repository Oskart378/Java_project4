/*---------------------------------------------------
Name: Oscar Lopez, Enoc Bernal, Brenda Benitez
COP 2805C - Java Programming 2
Fall 2025 - W 5:30 PM - 8:50 PM
Assignment # 4
Plagiarism Statement
I certify that this assignment is my own work and that I have not
copied in part or whole or otherwise plagiarized the work of other
students, persons, Generative Pre-trained Generators (GPTs) or any other AI tools.
I understand that students involved in academic dishonesty will face
disciplinary sanctions in accordance with the College's Student Rights
and Responsibilities Handbook (https://www.mdc.edu/rightsandresponsibilities)
01234567890123456789012345678901234567890123456789012345678901234567890123456789
----------------------------------------------------------*/

package com.example.mdctechsupport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SupportDesk {

    private final Queue<Ticket> activeTickets;
    private final Stack<Ticket> resolvedTickets;

    private final ObservableList<Ticket> activeTicketsObservable;
    private final ObservableList<Ticket> resolvedTicketsObservable;

    public SupportDesk() {
        activeTickets = new LinkedList<>();
        resolvedTickets = new Stack<>();

        activeTicketsObservable = FXCollections.observableArrayList(activeTickets);
        resolvedTicketsObservable = FXCollections.observableArrayList(resolvedTickets);
    }

    private void updateLists() {
        activeTicketsObservable.setAll(activeTickets);
        resolvedTicketsObservable.setAll(resolvedTickets);
    }

    public void addTicket(Ticket t) {
        activeTickets.add(t);
       updateLists(); // keep ObservableList in sync
    }

    public boolean processNextTicket() {
        if (activeTickets.isEmpty())
            return false;

        Ticket removed = activeTickets.remove();
        resolvedTickets.push(removed);

        // Update observable lists
//        activeTicketsObservable.remove(removed);
//        resolvedTicketsObservable.add(removed);
        updateLists();
        return true;
    }

    public boolean reopenLastResolved() {
        if (resolvedTickets.isEmpty())
            return false;

        Ticket t = resolvedTickets.pop();
        resolvedTicketsObservable.remove(t);

        activeTickets.add(t);
        updateLists();
        return true;
    }

    // Getter methods for TableView binding
    public ObservableList<Ticket> getActiveTicketsObservable() {
        return activeTicketsObservable;
    }

    public ObservableList<Ticket> getResolvedTicketsObservable() {
        return resolvedTicketsObservable;
    }
}