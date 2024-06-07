package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.UserModel;

public abstract class AbstractHomeController {

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
        confirmation.initStyle(StageStyle.UTILITY);
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Sei sicuro di volerti disconnettere?");
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                userModel.clearCurrentUser();
                //Pulizia in caso si fosse in una modifica o creazione di un nuovo modello
                CatalogoModel catalogoModel = CatalogoModel.getInstance();
                catalogoModel.setSelectedAuto(null);
                catalogoModel.setTempAuto(null);
                loadLoginPage();
            }
        });
    }

    private void loadLoginPage() {
        PageLoader.loadPage("/org/example/configuratoreauto/loginPage.fxml","Login", (Stage) mainPage.getScene().getWindow());
    }

    protected abstract void customizeLogoutTab();
}
