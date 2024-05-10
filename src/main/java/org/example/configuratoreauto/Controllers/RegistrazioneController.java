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
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

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

        //Agg
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

        //Disabilito il bottone registra, nel caso in cui l'input non sia valido. Manteniamo così dei dati consistenti
        registraButton.disableProperty().bind(isInputValid.not());
    }

    /*
    *   Funzione registraUtente: tenta di effettuare la registrazione utente con i dati inseriti.
    *       - Segnala eventuali errori a schermo
    *       - In caso la registrazione avvenga correttamente, setta currentUser all'utente appena registrato
    * */
    @FXML
    protected void registraUtente() {
        responseText.setText("");

        /* Recupero i dati inseriti nel form */
        String emailText = email.getText();
        String passText = password.getText();
        String passConfirmationText = passwordConferma.getText();
        String nomeText = nome.getText();
        String cognomeText = cognome.getText();

        //Se tutti i campi sono valorizzati:
        if(isInputValid.get()) {
            if (passText.equals(passConfirmationText)) {
                if (u.registraCliente(new Cliente(emailText, passText, nomeText, cognomeText))) {
                    responseText.setTextFill(Color.GREEN);
                    responseText.setText("Cliente registrato correttamente!");
                    //Salvo l'utente appena registrato come CurrentUser
                    u.validation(emailText, passText);

                } else {
                    responseText.setTextFill(Color.RED);
                    email.clear();
                    //Svuoto il campo email, mantenendo gli altri occupati
                    responseText.setText("Email già in uso!");
                }
            } else {
                responseText.setTextFill(Color.RED);
                passwordConferma.clear();
                responseText.setText("Attenzione! Le due password non combaciano");
            }
        }
        else if(!isEmailValid.get()){
            responseText.setTextFill(Color.RED);
            responseText.setText("Attenzione! Email non valida");
        }
    }
    /*
    *   Controllo sulla validità dell'email inserita
    * */
    public static boolean isValidEmail(String email) {
        System.out.println(email);
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
}
