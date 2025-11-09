package com.example.mdctechsupport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.Stack;

public class SupportDesk {

    private final LinkedList<Ticket> activeTickets;
    private final Stack<Ticket> resolvedTickets;

    private final ObservableList<Ticket> activeTicketsObservable;
    private final ObservableList<Ticket> resolvedTicketsObservable;

    public SupportDesk() {
        activeTickets = new LinkedList<>();
        resolvedTickets = new Stack<>();

        activeTicketsObservable = FXCollections.observableArrayList(activeTickets);
        resolvedTicketsObservable = FXCollections.observableArrayList(resolvedTickets);
    }

    public void addTicket(Ticket t) {
        activeTickets.addLast(t);
        activeTicketsObservable.add(t); // keep ObservableList in sync
    }

    public boolean processNextTicket() {
        if (activeTickets.isEmpty())
            return false;

        Ticket removed = activeTickets.removeFirst();
        resolvedTickets.push(removed);

        // Update observable lists
        activeTicketsObservable.remove(removed);
        resolvedTicketsObservable.add(removed);
        return true;
    }

    public boolean reopenLastResolved() {
        if (resolvedTickets.isEmpty())
            return false;

        Ticket t = resolvedTickets.pop();
        resolvedTicketsObservable.remove(t);

        activeTickets.addLast(t);
        activeTicketsObservable.add(t);
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