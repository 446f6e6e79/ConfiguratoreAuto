package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import static org.example.configuratoreauto.Main.setPage;

public class LoginController {
    UserModel userModel = UserModel.getInstance();

    @FXML
    private Label responseText;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private ReadOnlyBooleanWrapper isInputValid = new ReadOnlyBooleanWrapper(true);

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() {

        /*Leghiamo il valore di isInputEmpty ai due campi di input
        *   - TRUE se entrambi i campi sono vuoti
        *   - FALSE se almeno uno due campi non è vuoto
        * */
        isInputValid.bind(email.textProperty().isEmpty().and(password.textProperty().isEmpty()));

        //Rendiamo invisibile il testo non appena viene inserito un nuovo carattere
        responseText.visibleProperty().bind(isInputValid);

        /*
        *   Observer Pattern:
        *       - SOGGETTI: Campi email e password
        *       - Ad ogni cambiamento, nei due campi, la pagina reagirà cancellando il valore del responseText
        * */
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            responseText.setText("");
        });
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            responseText.setText("");
        });
        /*
        *   Quando il pulsante invio è premuto, viene effettuato tentativo di login
        * */
        email.setOnAction(event -> logIn());
        password.setOnAction(event -> logIn());
    }


    @FXML
    protected void onGuestClick() {
        setPage("clienteView/homepageCliente");
    }

    @FXML
    protected void logIn() {
        String emailText = email.getText();
        String passText = password.getText();
        email.clear();
        password.clear();
        if(userModel.validation(emailText, passText)){
            if (userModel.getCurrentUser() instanceof Cliente) {
                setPage("clienteView/homepageCliente");
            }
            if (userModel.getCurrentUser() instanceof Impiegato) {
                setPage("impiegatoView/homepage");
            }
            if (userModel.getCurrentUser() instanceof Segretario) {
                setPage("segretarioView/homepage");
            }
        }
        else{
            responseText.setText("Email o Password errate");
        }
    }
}