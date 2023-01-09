package com.oos.praktikum05;

import bank.*;
import bank.exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AccountController implements Initializable {

    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    private PrivateBank bank;

    @FXML
    public Text text;
    @FXML
    public MenuButton addButton;
    @FXML
    public MenuItem payment;
    @FXML
    public MenuItem incoming;
    @FXML
    public MenuItem outgoing;
    @FXML
    public Parent root;
    @FXML
    public MenuButton optionsButton;
    @FXML
    public MenuItem allTransaction;
    @FXML
    public MenuItem ascending;
    @FXML
    public MenuItem descending;
    @FXML
    public MenuItem positive;
    @FXML
    public MenuItem negative;
    @FXML
    public ListView<Transaction> transactionsListView;
    @FXML
    public Text accountName;
    @FXML
    public Button backButton;

    private void updateListView(List<Transaction> listTransaction) {
        transactionsList.clear();
        transactionsList.addAll(listTransaction);
        transactionsListView.setItems(transactionsList);
    }

    private void setDialogAddTransaction(MenuItem menuItem, String name) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.getDialogPane().setMinWidth(500);
        dialog.getDialogPane().setMinHeight(500);
        GridPane gridPane = new GridPane();

        Label date = new Label("Datum: ");
        Label description = new Label("Beschreibung: ");
        Label amount = new Label("Betrag: ");
        Label incomingInterest_sender = new Label();
        Label outgoingInterest_recipient = new Label();

        TextField dateText = new TextField();
        TextField descriptionText = new TextField();
        TextField amountText = new TextField();
        TextField incomingInterest_senderText = new TextField();
        TextField outgoingInterest_recipientText = new TextField();

        gridPane.add(date, 1, 1);
        gridPane.add(dateText, 2, 1);
        gridPane.add(description, 1, 2);
        gridPane.add(descriptionText, 2, 2);
        gridPane.add(amount, 1, 3);
        gridPane.add(amountText, 2, 3);
        gridPane.add(incomingInterest_sender, 1, 4);
        gridPane.add(incomingInterest_senderText, 2, 4);
        gridPane.add(outgoingInterest_recipient, 1, 5);
        gridPane.add(outgoingInterest_recipientText, 2, 5);

        ButtonType okButton = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResizable(true);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        Alert invalid = new Alert(Alert.AlertType.ERROR);
        dialog.show();
        if (Objects.equals(menuItem.getId(), "payment")) {
            dialog.setTitle("Payment hinzufügen");
            dialog.setHeaderText("Füge [" + name + "] ein neues Payment hinzu.");

            incomingInterest_sender.setText("Incoming interest: ");
            outgoingInterest_recipient.setText("Outgoing interest: ");

            dialog.setResultConverter(buttonType ->  {
                if (buttonType == okButton) {
                    if (Objects.equals(dateText.getText(), "") ||
                            Objects.equals(descriptionText.getText(),"") ||
                            Objects.equals(amountText.getText(), "") ||
                            Objects.equals(incomingInterest_senderText.getText(), "") ||
                            Objects.equals(outgoingInterest_recipientText.getText(), ""))
                    {
                        invalid.setContentText("Bitte geben sie einen gültigen Wert ein!");
                        Optional<ButtonType> result = invalid.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            text.setText("Es wurde kein Payment hinzugefügt.!");
                        }
                    } else {
                        Payment payment = null;
                        try {
                            payment = new Payment(dateText.getText(),
                                    descriptionText.getText(),
                                    Double.parseDouble(amountText.getText()),
                                    Double.parseDouble(incomingInterest_senderText.getText()),
                                    Double.parseDouble(outgoingInterest_recipientText.getText()));
                        } catch (ZinsenException e) {
                            throw new RuntimeException(e);
                        } catch (BetragException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            bank.addTransaction(name, payment);
                            text.setText("Es wurde ein neues Payment hinzugefügt.");
                        } catch (TransactionAlreadyExistException e) {
                            invalid.setContentText("Doppelter Payment Eintrag erkannt!");
                            Optional<ButtonType> result = invalid.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                text.setText("Dieses Payment ist bereits in der Bank vorhanden!");
                            }
                            System.out.println(e.getMessage());
                        } catch (AccountDoesNotExistException e) {
                            System.out.println(e.getMessage());
                        } catch (ZinsenException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            updateListView(bank.getTransactions(name));
                        } catch (AccountDoesNotExistException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            accountName.setText(name + " [" + bank.getAccountBalance(name) + "€]");
                        } catch (AccountDoesNotExistException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                return null;
            });
        }  else {
            incomingInterest_sender.setText("Sender: ");
            outgoingInterest_recipient.setText("Empfänger: ");
            if (Objects.equals(menuItem.getId(), "incoming")) {

                dialog.setTitle("Eingehender Transfer hinzufügen");
                dialog.setHeaderText("Eingehender Transfer für [" + name + "] hinzufügen.");

                dialog.setResultConverter(buttonType -> {
                    if (buttonType == okButton) {
                        if (Objects.equals(dateText.getText(), "") ||
                                Objects.equals(descriptionText.getText(),"") ||
                                Objects.equals(amountText.getText(), "") ||
                                Objects.equals(incomingInterest_senderText.getText(), "") ||
                                Objects.equals(outgoingInterest_recipientText.getText(), ""))
                        {
                            invalid.setContentText("Bitte geben sie einen gültigen Wert ein!");
                            Optional<ButtonType> result = invalid.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                text.setText("Es wurde kein Eingehender Transfer angelegt!");
                            }
                        } else {
                            IncomingTransfer incomingTransfer = null;
                            try {
                                incomingTransfer = new IncomingTransfer(dateText.getText(),
                                        descriptionText.getText(),
                                        Double.parseDouble(amountText.getText()),
                                        incomingInterest_senderText.getText(),
                                        outgoingInterest_recipientText.getText());
                            } catch (BetragException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                bank.addTransaction(name, incomingTransfer);
                                text.setText("Der Eingehende Transfer wurde hinzugefügt.");
                            } catch (TransactionAlreadyExistException e) {
                                invalid.setContentText("Doppelter Eingehender Transfer erkannt!");
                                Optional<ButtonType> result = invalid.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    text.setText("Dieser Eingehende Transfer ist bereits in der Bank vorhanden!");
                                }
                                System.out.println(e.getMessage());
                            } catch (AccountDoesNotExistException e) {
                                System.out.println(e.getMessage());
                            } catch (ZinsenException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                updateListView(bank.getTransactions(name));
                            } catch (AccountDoesNotExistException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                accountName.setText(name + " [" + bank.getAccountBalance(name) + "€]");
                            } catch (AccountDoesNotExistException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                    return null;
                });
            } else  {
                dialog.setTitle("Neue Ausgehende Transaktion hinzufügen");
                dialog.setHeaderText("Füge eine neue Ausgehende Transaktion dem Account [" + name + "] hinzu.");

                dialog.setResultConverter(buttonType -> {
                    if (buttonType == okButton) {
                        if (Objects.equals(dateText.getText(), "") ||
                                Objects.equals(descriptionText.getText(),"") ||
                                Objects.equals(amountText.getText(), "") ||
                                Objects.equals(incomingInterest_senderText.getText(), "") ||
                                Objects.equals(outgoingInterest_recipientText.getText(), ""))
                        {
                            invalid.setContentText("Bitte geben sie einen gültigen Wert an!");
                            Optional<ButtonType> result = invalid.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                text.setText("Kein neuer Ausgehende Transaktion wurde hinzugefügt!");
                            }
                        } else {
                            OutgoingTransfer outgoingTransfer = null;
                            try {
                                outgoingTransfer = new OutgoingTransfer(dateText.getText(),
                                        descriptionText.getText(),
                                        Double.parseDouble(amountText.getText()),
                                        incomingInterest_senderText.getText(),
                                        outgoingInterest_recipientText.getText());
                            } catch (BetragException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                bank.addTransaction(name, outgoingTransfer);
                                text.setText("Neue Ausgehende Transaktion hinzufügen.");
                            } catch (TransactionAlreadyExistException e) {
                                invalid.setContentText("Doppelte Ausgehende Transaktion erkannt!");
                                Optional<ButtonType> result = invalid.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    text.setText("Diese Ausgehende Transaktion ist bereits in der Bank vorhanden!");
                                }
                                System.out.println(e.getMessage());
                            } catch (AccountDoesNotExistException e) {
                                System.out.println(e.getMessage());
                            } catch (ZinsenException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                updateListView(bank.getTransactions(name));
                                accountName.setText(name + " [" + bank.getAccountBalance(name) + "€]");
                            } catch (AccountDoesNotExistException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    return null;
                });
            }
        }
    }

    public void setupData(PrivateBank privateBank, String name) throws AccountDoesNotExistException {
        bank = privateBank;
        accountName.setText(name + " [" + bank.getAccountBalance(name) + "€]");
        updateListView(bank.getTransactions(name));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteTransaction = new MenuItem("Transaktion löschen");

        contextMenu.getItems().addAll(deleteTransaction);
        transactionsListView.setContextMenu(contextMenu);

        AtomicReference<Transaction> selectedTransaction = new AtomicReference<>();

        transactionsListView.setOnMouseClicked(mouseEvent -> {
            selectedTransaction.set(transactionsListView.getSelectionModel().getSelectedItem());
            System.out.println("[" + selectedTransaction.toString().replace("\n", "]"));
        });
        deleteTransaction.setOnAction(event -> {

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Transaktion löschen.");
            confirmation.setContentText("Möchten sie wirklich diese Transaktion löschen?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    bank.removeTransaction(name, selectedTransaction.get());
                } catch (TransactionDoesNotExistException e) {
                    e.printStackTrace();
                } catch (ZinsenException e) {
                    throw new RuntimeException(e);
                } catch (AccountDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("[" + selectedTransaction.toString().replace("\n", "]") + " wurde gelöscht.");
                text.setText("[" + selectedTransaction.toString().replace("\n", "]") + " wurde gelöscht.");
                try {
                    updateListView(bank.getTransactions(name));
                } catch (AccountDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
                try {
                    accountName.setText(name + " [" + bank.getAccountBalance(name) + "€]");
                } catch (AccountDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        allTransaction.setOnAction(event -> {
            try {
                updateListView(bank.getTransactions(name));
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        });
        ascending.setOnAction(event -> {
            try {
                updateListView(bank.getTransactionsSorted(name, true));
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        });
        descending.setOnAction(event -> {
            try {
                updateListView(bank.getTransactionsSorted(name, false));
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        });
        positive.setOnAction(event -> updateListView(bank.getTransactionsByType(name, true)));
        negative.setOnAction(event -> updateListView(bank.getTransactionsByType(name, false)));

        payment.setOnAction(event -> setDialogAddTransaction(payment, name));
        incoming.setOnAction(event -> setDialogAddTransaction(incoming, name));
        outgoing.setOnAction(event -> setDialogAddTransaction(outgoing, name));



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        backButton.setOnMouseClicked(mouseEvent -> {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setTitle("Volksbank | Verwaltung");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}
