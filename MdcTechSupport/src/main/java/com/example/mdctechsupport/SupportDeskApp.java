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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SupportDeskApp extends Application {

    private SupportDesk supportDesk;

    // Form fields
    private TextField ticketIdTf;
    private TextField requesterNameTf;
    private TextArea issueTf;
    private ComboBox<String> priorityCb;

    // Tables
    private TableView<Ticket> activeTicketsTable;
    private TableView<Ticket> resolvedTicketsTable;

    @Override
    public void start(Stage primaryStage) throws IOException {
        supportDesk = new SupportDesk();

        BorderPane mainPane = new BorderPane();
        mainPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(245, 247, 250), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        mainPane.setTop(createHeader());
        mainPane.setLeft(createAddTicketForm());
        mainPane.setCenter(createTicketsDisplay());

        Scene scene = new Scene(mainPane, 800, 580);
        primaryStage.setTitle("MDC Tech Support Ticket System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* ---------------- HEADER ---------------- */
    private Pane createHeader() {
        Label mdcLabel = new Label("MDC Tech Support Ticket System");
        mdcLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        mdcLabel.setTextFill(Color.rgb(245, 245, 245));
        mdcLabel.setPadding(new Insets(20, 0, 20, 0));

        StackPane headerPane = new StackPane(mdcLabel);
        headerPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(74, 144, 226), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        return headerPane;
    }

    /* ---------------- FORM ---------------- */
    private Pane createAddTicketForm() {
        // Main container
        VBox formPane = createFormContainer();
        formPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(232, 238, 247), new CornerRadii(8), Insets.EMPTY)
        ));
        formPane.setBorder(new Border(new BorderStroke(
                Color.rgb(200, 210, 225), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)
        )));

        // Form title
        Label formNameLabel = createSectionLabel("New Ticket");

        // Ticket ID
        Label ticketIdLbl = new Label("Ticket ID");
        ticketIdTf = new TextField();
        ticketIdTf.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null
        ));
        ticketIdTf.setPromptText("Ex: 25");

        Label ticketIdError = createErrorLabel("* This ID is already taken");
        ticketIdTf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateTicketId(ticketIdError);
        });
        ticketIdTf.setOnKeyTyped(e -> resetField(ticketIdTf, ticketIdError));

        VBox ticketIdBox = new VBox(2, ticketIdLbl, ticketIdTf, ticketIdError); // small spacing 2

        // Requester Name
        Label requesterNameLbl = new Label("Requester Name");
        requesterNameTf = new TextField();
        requesterNameTf.setOnKeyTyped(e -> resetField(requesterNameTf, null));
        VBox requesterBox = new VBox(2, requesterNameLbl, requesterNameTf);

        // Issue Description
        Label issueLbl = new Label("Issue Description");
        issueTf = new TextArea();
        issueTf.setWrapText(true);
        issueTf.setPrefRowCount(6);
        issueTf.setPrefWidth(200);
        issueTf.setOnKeyTyped(e -> resetField(issueTf, null));
        VBox issueBox = new VBox(2, issueLbl, issueTf);

        // Priority
        Label priorityLbl = new Label("Priority");
        priorityCb = new ComboBox<>();
        priorityCb.getItems().addAll("Low", "Medium", "High");
        priorityCb.setValue("Low");
        VBox priorityBox = new VBox(2, priorityLbl, priorityCb);

        // Buttons
        Button addTicketBtn = createWideButton("➕ Add Ticket", e -> addTicket());
        Button exitBtn = createWideButton("❌ Exit/Close", e -> displayExitMessage());
        VBox buttonBox = new VBox(5, addTicketBtn, exitBtn); // small spacing between buttons

        // Add all to main form pane
        formPane.getChildren().addAll(
                formNameLabel,
                ticketIdBox,
                requesterBox,
                issueBox,
                priorityBox,
                buttonBox
        );

        StackPane wrapperPane = new StackPane(formPane);
        wrapperPane.setPadding(new Insets(20));

        return wrapperPane;
    }



    private VBox createFormContainer() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setBorder(new Border(new BorderStroke(
                Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2)
        )));
        return box;
    }

    /* ---------------- TICKETS DISPLAY ---------------- */
    private Pane createTicketsDisplay() {
        VBox ticketDisplayPane = createFormContainer();
        ticketDisplayPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(240, 243, 248), new CornerRadii(8), Insets.EMPTY)
        ));
        ticketDisplayPane.setBorder(new Border(new BorderStroke(
                Color.rgb(200, 210, 225), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)
        )));

        Label activeLbl = createSectionLabel("Active Tickets");
        Label resolvedLbl = createSectionLabel("Recently Resolved Tickets");

        activeTicketsTable = createTicketTable();
        resolvedTicketsTable = createTicketTable();

        // Set preferred heights based on number of visible rows
        int activeRows = 6; // show 6 rows for active tickets
        int resolvedRows = 4; // show 4 rows for recently resolved
        double rowHeight = 25; // approximate row height
        double headerHeight = 28; // approximate header height

        activeTicketsTable.setPrefHeight(activeRows * rowHeight + headerHeight);
        resolvedTicketsTable.setPrefHeight(resolvedRows * rowHeight + headerHeight);

        // Make tables grow vertically if space allows
        VBox.setVgrow(activeTicketsTable, Priority.ALWAYS);
        VBox.setVgrow(resolvedTicketsTable, Priority.ALWAYS);

        activeTicketsTable.setItems(supportDesk.getActiveTicketsObservable());
        resolvedTicketsTable.setItems(supportDesk.getResolvedTicketsObservable());

        Button processNextBtn = new Button("▶ Process Next");
        Button reopenLastBtn = new Button("\uD83D\uDD04 Reopen Last");

        processNextBtn.setOnAction(e -> processNextTicket());
        reopenLastBtn.setOnAction(e -> reopenLastResolved());

        ticketDisplayPane.getChildren().addAll(
                activeLbl, processNextBtn, activeTicketsTable,
                resolvedLbl, reopenLastBtn, resolvedTicketsTable
        );

        StackPane wrapper = new StackPane(ticketDisplayPane);
        wrapper.setPadding(new Insets(20));
        return wrapper;
    }

    private TableView<Ticket> createTicketTable() {
        TableView<Ticket> table = new TableView<>();
        table.getColumns().addAll(createTicketColumns());
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private List<TableColumn<Ticket, ?>> createTicketColumns() {

        //tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableColumn<Ticket, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idCol.setSortable(false);

        TableColumn<Ticket, String> requesterCol = new TableColumn<>("Requester");
        requesterCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        requesterCol.setSortable(false);

        TableColumn<Ticket, String> issueCol = new TableColumn<>("Issue");
        issueCol.setCellValueFactory(cellData -> cellData.getValue().issueProperty());
        issueCol.setSortable(false);

        TableColumn<Ticket, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        priorityCol.setSortable(false);


        idCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.1);       // 10%
        requesterCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.3); // 30%
        issueCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.45);     // 40%
        priorityCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.15);  // 20%

        return Arrays.asList(idCol, requesterCol, issueCol, priorityCol);
    }

    /* ---------------- LOGIC ---------------- */
    private void addTicket() {
        try {
            int id = Integer.parseInt(ticketIdTf.getText());
            String name = requesterNameTf.getText();
            String issue = issueTf.getText();
            String priority = priorityCb.getValue();

            supportDesk.addTicket(new Ticket(id, name, issue, priority));

            clearFields();
            clearFieldStyles();
            showAlert("Ticket with ID " + id + " successfully added!");

        } catch (NumberFormatException ex) {
            markFieldError(ticketIdTf, "ID must be a valid number");
        }

        catch (IllegalArgumentException ex) {
            highlightFieldByError(ex.getMessage());
        }

        catch (Exception ex) {
            highlightFieldByError("Error!");
        }
    }

    private void processNextTicket() {
        if (!supportDesk.processNextTicket())
            showAlert("There are no more tickets to process!");
    }

    private void reopenLastResolved() {
        if (!supportDesk.reopenLastResolved())
            showAlert("There are no more tickets to reopen!");
    }

    /* ---------------- HELPERS ---------------- */
    private void clearFields() {
        ticketIdTf.clear();
        requesterNameTf.clear();
        issueTf.clear();
        priorityCb.setValue("Low");
    }

    private void clearFieldStyles() {
        ticketIdTf.setStyle(null);
        requesterNameTf.setStyle(null);
        issueTf.setStyle(null);
    }

    private void resetField(Control field, Label errorLabel) {
        field.setStyle(null);
        if (errorLabel != null) errorLabel.setVisible(false);
    }

    private void validateTicketId(Label errorLabel) {
        try {
            int id = Integer.parseInt(ticketIdTf.getText());
            boolean taken = !Ticket.isIdAvailable(id);
            errorLabel.setVisible(taken);
            ticketIdTf.setStyle(taken ? "-fx-border-color: red;" : null);
        } catch (NumberFormatException ignored) {}
    }

    private void markFieldError(Control field, String message) {
        field.setStyle("-fx-border-color: red;");
        field.requestFocus();
        showAlert(message);
    }

    private void highlightFieldByError(String message) {
        String msg = message.toLowerCase();
        if (msg.contains("id")) markFieldError(ticketIdTf, message);
        else if (msg.contains("name")) markFieldError(requesterNameTf, message);
        else if (msg.contains("issue")) markFieldError(issueTf, message);
        else showAlert(message);
    }

    private void displayExitMessage() {
        showAlert("Thank you for using MDC Tech Support Ticket System!");
        Platform.exit();
    }

    /* ---------------- UI UTILITIES ---------------- */
    private Label createSectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lbl.setAlignment(Pos.CENTER);
        return lbl;
    }

    private Label createErrorLabel(String text) {
        Label lbl = new Label(text);
        lbl.setTextFill(Color.RED);
        lbl.setVisible(false);
        lbl.managedProperty().bind(lbl.visibleProperty());
        return lbl;
    }

    private Button createWideButton(String text, EventHandler<ActionEvent> action){
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(action);
        VBox.setVgrow(btn, Priority.ALWAYS);

        return btn;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
