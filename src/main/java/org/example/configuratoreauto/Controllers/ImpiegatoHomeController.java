package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

public class ImpiegatoHomeController {
    UserModel userModel = UserModel.getInstance();

    @FXML
    private TabPane mainPage;

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    @FXML
    private void logout() {
        mainPage.getSelectionModel().selectFirst();
        // Create a confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Sei sicuro di volerti disconnettere?");


        // Customize the buttons in the dialog
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                UserModel userModel = UserModel.getInstance();
                userModel.clearCurrentUser();
                // User clicked OK, proceed with logout
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/loginPage.fxml"));
                    VBox loginPane = loader.load();
                    Stage stage = (Stage) mainPage.getScene().getWindow();

                    Scene scene = new Scene(loginPane);
                    stage.setScene(scene);
                    stage.setTitle("Login");

                    stage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}