package org.example.configuratoreauto;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.configuratoreauto.Utenti.UserModel;

public class Controller {
    UserModel userModel = UserModel.getInstance();

    @FXML
    private Label responseText;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private ReadOnlyBooleanWrapper isInputEmpty = new ReadOnlyBooleanWrapper(true);

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() {

        /*Leghiamo il valore di isInputEmpty ai due campi di input
        *   - TRUE se entrambi i campi sono vuoti
        *   - FALSE se almeno uno due campi non è vuoto
        * */
        isInputEmpty.bind(email.textProperty().isEmpty().and(password.textProperty().isEmpty()));

        //Rendiamo invisibile il testo non appena viene inserito un nuovo carattere
        responseText.visibleProperty().bind(isInputEmpty);

        /*
        *   Quando il pulsante invio è premuto, viene effettuato tentativo di login
        * */
        email.setOnAction(event -> logIn());
        password.setOnAction(event -> logIn());
    }


    @FXML
    protected void onGuestClick() {
        responseText.setText("Accesso effettuato come ospite");
    }

    @FXML
    protected void logIn() {
        String emailText = email.getText();
        String passText = password.getText();
        System.out.println("Email: "+emailText+", password: "+passText);
        email.clear();
        password.clear();

        if(userModel.validation(emailText, passText)){
            responseText.setText("Benvenuto "+ userModel.getCurrentUser().toString());
        }
        else{
            responseText.setText("Email o Password errate");
        }
    }
}