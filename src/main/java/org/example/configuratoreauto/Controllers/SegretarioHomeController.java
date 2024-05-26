package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.SediModel;

import java.io.IOException;


public class SegretarioHomeController {

    RegistroModel registro = RegistroModel.getInstance();
    BorderPane preventiviNode;
    PreventiviController preventiviController;
    @FXML
    private TabPane mainPage;

    @FXML
    private void initialize() throws InterruptedException {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        try{
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            //Setting dinamico delle dimensioni della pagina preventivi
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
            preventiviController = preventiviLoader.getController();
            //GET 0 MOMENTANEO
            preventiviController.loadPrevs(registro.getPreventiviBySede(SediModel.getInstance().getAllData().get(0)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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