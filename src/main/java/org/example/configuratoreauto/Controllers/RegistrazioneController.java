package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrazioneController {
    UserModel u = UserModel.getInstance();
    private static final String EMAIL_PATTERN =
            "[a-zA-Z]+\\.[a-zA-Z]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

    @FXML
    private Label responseText;
    @FXML
    private TextField nome;
    @FXML
    private TextField cognome;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordConferma;
    @FXML
    private Button registraButton;

    private ReadOnlyBooleanWrapper isEmailValid = new ReadOnlyBooleanWrapper(false);
    private ReadOnlyBooleanWrapper isInputValid = new ReadOnlyBooleanWrapper(false);

    @FXML
    private void initialize() {
        /*
         *   Quando il pulsante invio è premuto, viene effettuato tentativo di login
         * */
        nome.setOnAction(event -> registraUtente());
        cognome.setOnAction(event -> registraUtente());
        email.setOnAction(event -> registraUtente());
        password.setOnAction(event -> registraUtente());
        passwordConferma.setOnAction(event -> registraUtente());

        /*
           Creo una lista di Listener. Viene cancellato il contenuto di responseText ogni qualvolta una
           textProperty, di quelle appartenenti alla lista, viene aggiornata
        */
        ChangeListener<String> textChangeListener = (observable, oldValue, newValue) -> {
            responseText.setText("");
        };

        nome.textProperty().addListener(textChangeListener);
        cognome.textProperty().addListener(textChangeListener);
        email.textProperty().addListener(textChangeListener);
        password.textProperty().addListener(textChangeListener);
        passwordConferma.textProperty().addListener(textChangeListener);

        email.textProperty().addListener((obs, oldText, newText) -> {
            if (isValidEmail(newText)) {
                isEmailValid.set(true);
            } else {
                isEmailValid.set(false);
            }
        });

        /*
        *   Lego il valore isInputValid dinamicamente alle altre proprietà elencate. L'input è considerato valido se:
        *       - L'email è valida
        *       - tutti gli altri campi sono non vuoti
        * */
        isInputValid.bind(isEmailValid
                .and(password.textProperty().isNotEmpty())
                .and(passwordConferma.textProperty().isNotEmpty())
                .and(nome.textProperty().isNotEmpty())
                .and(cognome.textProperty().isNotEmpty()));

        //Disabilito il bottone registra, nel caso in cui l'input non sia valido. Manteniamo così dei dati coerenti
        registraButton.disableProperty().bind(isInputValid.not());
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
                    responseText.setTextFill(Color.GREEN);
                    responseText.setText("Cliente registrato correttamente!");
                    //Salvo l'utente appena registrato come CurrentUser
                    u.validation(emailText, passText);

                } else {
                    responseText.setTextFill(Color.RED);
                    responseText.setText("Email già in uso!");
                    //Svuoto il campo email, mantenendo gli altri occupati
                    email.clear();
                }
            } else {
                responseText.setTextFill(Color.RED);
                responseText.setText("Attenzione! Le due password non combaciano");
                passwordConferma.clear();
            }
        }
        else if(!isEmailValid.get()){
            responseText.setTextFill(Color.RED);
            responseText.setText("Attenzione! Email non valida");
        }
        else {
            responseText.setTextFill(Color.RED);
            responseText.setText("Attenzione! Devono essere inseriti tutti i campi");
        }
    }
    /*
    *   Controllo che la mail inserita sia valida
    * */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
