package com.example.mdctechsupport;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {

    private SupportDesk supportDesk;

    private TextField ticketIdTf;
    private TextField requesterNameTf;
    private TextArea issueTf;
    private ComboBox<String> priorityCb;

    private Button addTicketBtn;
    private Button exitBtn;

    TableView<Ticket> activeTicketsTable;
    TableView<Ticket> resolvedTicketsTable;

    @Override
    public void start(Stage primaryStage) throws IOException {

        supportDesk = new SupportDesk();
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(createHeader());
        mainPane.setLeft(createAddTicketForm());
        mainPane.setCenter(createTicketsDisplay());


        Scene scene = new Scene(mainPane, 800, 580);
        primaryStage.setTitle("Mdc Tech Support System");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private Pane createHeader() {

        StackPane headerPane = new StackPane();
        headerPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(74, 144, 226),  // your background color
                new CornerRadii(0),        // 0 for square corners
                Insets.EMPTY)));

        javafx.scene.control.Label mdcLabel = new Label("MDC Tech Support Ticket System");
        mdcLabel.setPadding(new Insets(20, 0, 20, 0));
        mdcLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD ,24));
        mdcLabel.setTextFill(Color.rgb(245, 245, 245));
        headerPane.getChildren().add(mdcLabel);

        return headerPane;

    }

    private Pane createAddTicketForm() {

        StackPane wrapperPane = new StackPane();
        wrapperPane.setPadding(new Insets(20));

        VBox formPane = new VBox(15);
        formPane.setBorder(new Border(new BorderStroke(
                Color.GRAY,                   // border color
                BorderStrokeStyle.SOLID,      // border style
                new CornerRadii(5),           // rounded corners, 0 for square
                new BorderWidths(2)           // thickness
        )));
        formPane.setPadding(new Insets(20));

        Label formNameLabel = new Label("New Ticket");
        formNameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        HBox formLabelWrapper = new HBox(formNameLabel);
        formLabelWrapper.setAlignment(Pos.CENTER);


        //Form Labels
        Label ticketIdLbl = new Label("Ticket ID");
        Label requesterNameLbl = new Label("Requester Name");
        Label issueDescriptionLbl = new Label("Issue Description");
        Label priorityLbl = new Label("Priority");

        //TextBoxes
        ticketIdTf = new TextField();
        ticketIdTf.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null
        ));

        Label ticketIdError = new Label("* This ID is already taken");
        ticketIdError.setTextFill(Color.RED);
        ticketIdError.setVisible(false);
        ticketIdError.managedProperty().bind(ticketIdError.visibleProperty());

        ticketIdTf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // lost focus
                try {
                    int id = Integer.parseInt(ticketIdTf.getText());
                    if (!Ticket.isIdAvailable(id)) {
                        ticketIdError.setVisible(true);
                        ticketIdTf.setStyle("-fx-border-color: red;");
                    } else {
                        ticketIdError.setVisible(false);
                        ticketIdTf.setStyle(null);

                    }
                } catch (NumberFormatException e) {
                    //ticketIdError.setVisible(true); // invalid number
                    //ticketIdTf.setStyle("-fx-border-color: red;");
                    //showAlert(e.getMessage());
                }
            }
        });

        ticketIdTf.setOnKeyTyped(e -> {
            ticketIdTf.setStyle("");
            ticketIdError.setVisible(false);
        });

        requesterNameTf = new TextField();
        requesterNameTf.setOnKeyTyped(e -> requesterNameTf.setStyle(""));

        issueTf = new TextArea();
        issueTf.setWrapText(true);
        issueTf.setPrefRowCount(6);
        issueTf.setPrefWidth(200);
        issueTf.setOnKeyTyped(e -> issueTf.setStyle(""));

        priorityCb =  new ComboBox<>();

        priorityCb.getItems().addAll("Low", "Medium", "High");
        priorityCb.setValue("Low");

        addTicketBtn = new Button("Add Ticket");
        addTicketBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(addTicketBtn, Priority.ALWAYS);
        addTicketBtn.setOnAction(e -> addTicket());

        exitBtn = new Button("Exit/Close");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(exitBtn, Priority.ALWAYS);

        exitBtn.setOnAction(e -> displayExitMessage());

        VBox ticketIdRow = new VBox(3, ticketIdLbl, ticketIdTf, ticketIdError);
        VBox requestNameRow = new VBox(3, requesterNameLbl, requesterNameTf);
        VBox issueRow = new VBox(3, issueDescriptionLbl, issueTf);
        VBox priorityRow = new VBox(3, priorityLbl, priorityCb);

        formPane.getChildren().addAll(formLabelWrapper,
                ticketIdRow,
                requestNameRow,
                issueRow,
                priorityRow,
                addTicketBtn, exitBtn);

        VBox.setVgrow(formNameLabel, Priority.ALWAYS);
        formNameLabel.setAlignment(Pos.CENTER);
        wrapperPane.getChildren().add(formPane);

        return wrapperPane;
    }

    private void displayExitMessage() {

        showAlert("Thank you for using MDC Tech Support Ticket System!");
        Platform.exit();
    }

    private Pane createTicketsDisplay() {

        StackPane wrapperPane = new StackPane();
        wrapperPane.setPadding(new Insets(20));

        VBox ticketDisplayPane = new VBox(15);
        ticketDisplayPane.setBorder(new Border(new BorderStroke(
                Color.GRAY,                   // border color
                BorderStrokeStyle.SOLID,      // border style
                new CornerRadii(5),           // rounded corners, 0 for square
                new BorderWidths(2)           // thickness
        )));
        ticketDisplayPane.setPadding(new Insets(20));

        Label activeTicketsLbl = new Label("Active Tickets");
        activeTicketsLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        Label recentlyResolvedTicketsLbl = new Label("Recently Resolved Tickets");
        recentlyResolvedTicketsLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        activeTicketsTable = new TableView<Ticket>();
        resolvedTicketsTable = new TableView<Ticket>();

        activeTicketsTable.getColumns().addAll(createTicketColumns());
        resolvedTicketsTable.getColumns().addAll(createTicketColumns());

        activeTicketsTable.setItems(supportDesk.getActiveTicketsObservable());
        resolvedTicketsTable.setItems(supportDesk.getResolvedTicketsObservable());

        activeTicketsTable.setEditable(false);
        resolvedTicketsTable.setEditable(false);

        activeTicketsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        resolvedTicketsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Control buttons
        Button processNextBtn = new Button("Process Next");
        Button reopenLastBtn = new Button("Reopen Last");

        processNextBtn.setOnAction(e -> processNextTicket());
        reopenLastBtn.setOnAction(e -> reopenLastResolved());

        ticketDisplayPane.getChildren().addAll(
                activeTicketsLbl, processNextBtn,activeTicketsTable,
                recentlyResolvedTicketsLbl, reopenLastBtn ,resolvedTicketsTable
        );

        wrapperPane.getChildren().add(ticketDisplayPane);

        return wrapperPane;
    }

    private List<TableColumn<Ticket, ?>> createTicketColumns()  {
        TableColumn<Ticket, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().getId());

        TableColumn<Ticket, String> requesterCol = new TableColumn<>("Requester");
        requesterCol.setCellValueFactory(cellData -> cellData.getValue().getName());

        TableColumn<Ticket, String> issueCol = new TableColumn<>("Issue");
        issueCol.setCellValueFactory(cellData -> cellData.getValue().getIssue());

        TableColumn<Ticket, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(cellData -> cellData.getValue().getPriority());

        return Arrays.asList(idCol, requesterCol, issueCol, priorityCol);
    }

    private void addTicket() {

        try {
            int id = Integer.parseInt(ticketIdTf.getText());
            String name = requesterNameTf.getText();
            String issue = issueTf.getText();
            String priority = priorityCb.getValue();

            supportDesk.addTicket(new Ticket(id, name, issue, priority));

            ticketIdTf.clear();
            requesterNameTf.clear();
            issueTf.clear();
            priorityCb.setValue("Low");

            clearFieldsErrors();
        }

        catch (NumberFormatException ex) {
            showAlert("ID must be a number");
            ticketIdTf.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            ticketIdTf.requestFocus();
        }

        catch (Exception ex) {

            String errorMessage = ex.getMessage().toLowerCase();

            if (errorMessage.contains("id")) {
                ticketIdTf.setStyle("-fx-border-color: red;");
                ticketIdTf.requestFocus();
                ticketIdTf.setStyle("-fx-border-color: red;");
            }

            else if (errorMessage.contains("name")) {
                requesterNameTf.setStyle("-fx-border-color: red;");
                requesterNameTf.requestFocus();
                requesterNameTf.setStyle("-fx-border-color: red;");

            }

            else if (errorMessage.contains("issue")) {
                issueTf.setStyle("-fx-border-color: red;");
                issueTf.requestFocus();
                issueTf.setStyle("-fx-border-color: red;");
            }

            else
                showAlert(ex.getMessage());
        }
    }

    private void validateRequiredField(TextInputControl field) {
        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // when the user leaves the field
                String text = field.getText();
                if (text == null || text.trim().isEmpty()) {
                    field.setStyle("-fx-border-color: red;");
                } else {
                    field.setStyle(null);
                }
            }
        });
    }


    private void clearFieldsErrors() {
        ticketIdTf.setStyle(null);
        requesterNameTf.setStyle(null);
        issueTf.setStyle(null);
    }

    private void processNextTicket() {

        boolean canProcessTicket = supportDesk.processNextTicket();

        if (!canProcessTicket) {
            showAlert("There are no more tickets to process!");
        }
    }

    private void reopenLastResolved() {

        boolean canReopenTicket = supportDesk.reopenLastResolved();

        if (!canReopenTicket) {
            showAlert("There are no more tickets to reopen!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null); // removes the header
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }
}