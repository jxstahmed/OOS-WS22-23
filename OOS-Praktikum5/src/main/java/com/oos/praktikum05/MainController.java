package com.oos.praktikum05;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.ZinsenException;
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
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;
    private final ObservableList<String> accountList = FXCollections.observableArrayList();
    private final PrivateBank volksbank;

    {
        try {
            volksbank = new PrivateBank("Volksbank", "Volksbank", 0.10, 0.05);
        } catch (ZinsenException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Text text;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> accountsListView;

    @FXML
    private Parent root;


    private void updateListView() {
        accountList.clear();
        accountList.addAll(volksbank.getAllAccounts());
        accountList.sort(Comparator.naturalOrder());
        accountsListView.setItems(accountList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        updateListView();

        // Rechtsklick Menü
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewAccount = new MenuItem("Account anzeigen");
        MenuItem deleteAccount = new MenuItem("Account löschen");

        contextMenu.getItems().addAll(viewAccount, deleteAccount);
        accountsListView.setContextMenu(contextMenu);

        // Damit automatisches Lesen und Schreiben mehrere Threads passieren kann.
        AtomicReference<String> selectedAccount = new AtomicReference<>();

        accountsListView.setOnMouseClicked(mouseEvent -> {
            selectedAccount.set(String.valueOf(accountsListView.getSelectionModel().getSelectedItems()));
            String account = selectedAccount.toString().replace("[", "").replace("]", "");
            System.out.println(selectedAccount + " wurde ausgewählt.");
            text.setText("Account " + selectedAccount + " wurde ausgewählt.");

            // Wenn Doppelklick auf Account Name, dann automatisch anzeigen.
            if (mouseEvent.getClickCount() == 2)
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("account-view.fxml")));
                    root = loader.load();

                    AccountController accountController = loader.getController();
                    accountController.setupData(volksbank, account);

                    stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AccountDoesNotExistException e) {
                    throw new RuntimeException(e);
                }

        });

        // Wenn der Account per Rechtsklick gelöscht wird.
        deleteAccount.setOnAction(event -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Löschbestätigung");
            confirmation.setContentText("Sind sie sicher, dass sie diesen Account löschen möchten?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    volksbank.deleteAccount(selectedAccount.toString().replace("[", "").replace("]", ""));
                } catch (AccountDoesNotExistException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(selectedAccount + " wurde gelöscht.");
                text.setText(selectedAccount + " wurde gelöscht.");
                updateListView();
            }
        });

        // Wenn der Account per Rechtsklick angezeigt werden soll.
        viewAccount.setOnAction(event -> {
            stage = (Stage) root.getScene().getWindow();

            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("account-view.fxml")));
                root = loader.load();

                AccountController accountController = loader.getController();
                accountController.setupData(volksbank, selectedAccount.toString().replace("[", "").replace("]", ""));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }

            scene = new Scene(root);
            stage.setTitle("Volksbank | Verwaltung");
            stage.setScene(scene);
            stage.show();
        });

        // Wenn der "Hinzufügen" Button gedrückt wird.
        addButton.setOnMouseClicked(event -> {
            text.setText("");
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Account hinzufügen.");
            dialog.setHeaderText("Neuen Account zur Bank hinzufügen.");
            dialog.getDialogPane().setMinWidth(300);


            Label nameLabel = new Label("Name: ");
            TextField nameTextFiel = new TextField();

            GridPane grid = new GridPane();
            grid.add(nameLabel, 2, 1);
            grid.add(nameTextFiel, 3, 1);
            dialog.getDialogPane().setContent(grid);
            dialog.setResizable(true);

            ButtonType buttonTypeOk = new ButtonType("Bestätigen", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == buttonTypeOk) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    if (!Objects.equals(nameTextFiel.getText(), "")) {

                        try {
                            volksbank.createAccount(nameTextFiel.getText());
                            text.setText("Account [" + nameTextFiel.getText() + "] wurde in der Bank erstellt.");
                        } catch (AccountAlreadyExistsException e) {
                            alert.setContentText("Dieser Account ist bereits vorhanden!");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                text.setText("Account [" + nameTextFiel.getText() + "] existiert bereits in dieser Bank.!");
                            }
                            System.out.println(e.getMessage());
                        }
                       updateListView();
                    }
                    else {
                        alert.setContentText("Bitte geben sie einen gültigen namen an!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            text.setText("Es wurde kein Account hinzugefügt!");
                        }
                    }

                }
                return null;
            });

            dialog.show();
        });
    }

}