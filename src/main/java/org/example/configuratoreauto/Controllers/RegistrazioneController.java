package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Persona;
import org.example.configuratoreauto.Utenti.UserModel;
import org.example.configuratoreauto.Controllers.PageLoader;

public class RegistrazioneController {
    private final UserModel u = UserModel.getInstance();
    private final ReadOnlyBooleanWrapper isEmailValid = new ReadOnlyBooleanWrapper(false);
    private final ReadOnlyBooleanWrapper isInputValid = new ReadOnlyBooleanWrapper(false);

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
    @FXML
    private Hyperlink login;

    @FXML
    private void initialize() {
        setupListeners();
        inputValidation();
        registraButton.disableProperty().bind(isInputValid.not());
    }

    private void setupListeners() {
        ChangeListener<String> textChangeListener = (observable, oldValue, newValue) -> responseText.setText("");
        nome.textProperty().addListener(textChangeListener);
        cognome.textProperty().addListener(textChangeListener);
        email.textProperty().addListener(textChangeListener);
        password.textProperty().addListener(textChangeListener);
        passwordConferma.textProperty().addListener(textChangeListener);

        email.textProperty().addListener((obs, oldText, newText) -> isEmailValid.set(Persona.isValidEmail(newText)));

        nome.setOnAction(event -> registraUtente());
        cognome.setOnAction(event -> registraUtente());
        email.setOnAction(event -> registraUtente());
        password.setOnAction(event -> registraUtente());
        passwordConferma.setOnAction(event -> registraUtente());
        login.setOnAction(event -> onLogin());
    }

    private void inputValidation() {
        isInputValid.bind(isEmailValid
                .and(password.textProperty().isNotEmpty())
                .and(passwordConferma.textProperty().isNotEmpty())
                .and(nome.textProperty().isNotEmpty())
                .and(cognome.textProperty().isNotEmpty()));
    }

    @FXML
    protected void registraUtente() {
        responseText.setText("");
        String emailText = email.getText();
        String passText = password.getText();
        String passConfirmationText = passwordConferma.getText();
        String nomeText = nome.getText();
        String cognomeText = cognome.getText();

        if (isInputValid.get()) {
            if (passText.equals(passConfirmationText)) {
                if (u.registraCliente(new Cliente(emailText, passText, nomeText, cognomeText))) {
                    responseText.setTextFill(Color.GREEN);
                    responseText.setText("Cliente registrato correttamente!");
                    u.validation(emailText, passText);
                    onLogin();
                } else {
                    showError("Email già in uso!");
                }
            } else {
                showError("Attenzione! Le due password non combaciano");
            }
        } else if (!isEmailValid.get()) {
            showError("Attenzione! Email non valida");
        }
    }

    private void showError(String message) {
        responseText.setTextFill(Color.RED);
        responseText.setText(message);
    }

    @FXML
    public void onLogin() {
        PageLoader.loadPage("/org/example/configuratoreauto/loginPage.fxml", "Login", (Stage) email.getScene().getWindow());
    }
}
