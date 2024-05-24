package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.configuratoreauto.Utenti.UserModel;

public class ImpiegatoHomeController {
    UserModel userModel = UserModel.getInstance();

    @FXML
    private Label responseText;

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() {

    }
}