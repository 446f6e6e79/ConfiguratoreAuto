package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import static java.lang.Thread.sleep;

public class ClienteController {
    UserModel userModel = UserModel.getInstance();
    CatalogoModel catalogo = CatalogoModel.getInstance();

    Cliente currentUser = (Cliente) userModel.getCurrentUser();

    @FXML
    private Label responseText;

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() throws InterruptedException {
        responseText.setText("Benvenuto, "+currentUser.getName()+"!");

    }
}