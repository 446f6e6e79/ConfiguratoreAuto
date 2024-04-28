package org.example.configuratoreauto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    UserModel userModel = UserModel.getInstance();
    @FXML
    private Label responseText;

    @FXML
    private TextField email;

    @FXML
    private TextField password;
    @FXML
    protected void onGuestClick() {
        responseText.setText("Accesso come ospite");
    }
    @FXML
    protected void onLoginClick() {
        String emailText = email.getText();
        String passText = password.getText();
        email.clear();
        password.clear();
        if(userModel.login(emailText, passText)){
            responseText.setText("Welcome to Application\n"+ userModel.getCurrentUser().toString());
        }
        else{
            responseText.setText("Email o Password errate");
        }
    }


}