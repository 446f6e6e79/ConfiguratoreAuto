package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import static org.example.configuratoreauto.Main.setPage;

public class RegistrazioneController {
    UserModel u = UserModel.getInstance();

    @FXML
    private Label responseText;

    @FXML
    private TextField nome;
    @FXML TextField cognome;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML PasswordField passwordConferma;

    private ReadOnlyBooleanWrapper isInputValid = new ReadOnlyBooleanWrapper(true);

    @FXML
    private void initialize() {

        /*Leghiamo il valore di isInputEmpty ai due campi di input
         *   - TRUE se entrambi i campi sono vuoti
         *   - FALSE se almeno uno due campi non è vuoto
         * */
        isInputValid.bind(email.textProperty().isEmpty().and(password.textProperty().isEmpty()));

        /*
         *   Quando il pulsante invio è premuto, viene effettuato tentativo di login
         * */
        email.setOnAction(event -> registraUtente());
        password.setOnAction(event -> registraUtente());
    }

    @FXML
    protected void registraUtente() {
        responseText.setText("");

        /* Recupero i dati inseriti nel form */
        String emailText = email.getText();
        String passText = password.getText();
        String passConfirmationText = passwordConferma.getText();
        String nomeText = nome.getText();
        String cognomeText = cognome.getText();

        //Se le due password sono uguali:
        if(isInputValid.get()) {
            if (passText.equals(passConfirmationText)) {
                if (u.registraCliente(new Cliente(emailText, passText, nomeText, cognomeText))) {
                    responseText.setText("Cliente registrato correttamente!");
                    //Salvo l'utente appena registrato come CurrentUser
                    u.validation(emailText, passText);
                } else {
                    responseText.setText("Email già in uso!");
                    //Svuoto il campo email, mantenendo gli altri occupati
                    email.clear();
                }
            } else {
                responseText.setText("Attenzione! Le due password non combaciano");
                passwordConferma.clear();
            }
        }
        else{
            responseText.setText("Tutti i campi devono essere valorizzati!");
        }
    }
}
