package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.example.configuratoreauto.Utenti.UserModel;

public class ImpiegatoHomeController {
    UserModel userModel = UserModel.getInstance();

    @FXML
    private TabPane mainPage;

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }
}