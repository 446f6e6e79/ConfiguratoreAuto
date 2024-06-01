package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

public abstract class BaseHomeController {

    protected UserModel userModel = UserModel.getInstance();

    @FXML
    protected TabPane mainPage;
    @FXML
    protected Tab logout;

    @FXML
    protected void initialize() {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        mainPage.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.equals(logout)) {
                mainPage.getSelectionModel().select(oldTab);
                logout();
            }
        });

        customizeLogoutTab();
    }

    @FXML
    protected void logout() {
        if(userModel.getCurrentUser() == null){
            loadLoginPage();
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Sei sicuro di volerti disconnettere?");
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                userModel.clearCurrentUser();
                loadLoginPage();
            }
        });
    }

    private void loadLoginPage() {
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

    protected abstract void customizeLogoutTab();
}
