package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.*;
import static org.example.configuratoreauto.Main.setPage;

public class LoginController {
    private final UserModel userModel = UserModel.getInstance();
    private final CatalogoModel catalogoModel = CatalogoModel.getInstance();
    private final ReadOnlyBooleanWrapper isInputValid = new ReadOnlyBooleanWrapper(false);
    @FXML
    private Label responseText;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button guestButton;

    @FXML
    private void initialize() {
        //Bind tra gli input ed i bottoni per bloccare azioni indesiderate
        isInputValid.bind(email.textProperty().isNotEmpty().and(password.textProperty().isNotEmpty()));
        login.disableProperty().bind(isInputValid.not());
        email.textProperty().addListener((obs, oldVal, newVal) -> responseText.setText(""));
        password.textProperty().addListener((obs, oldVal, newVal) -> responseText.setText(""));
        email.setOnAction(event -> logIn());
        password.setOnAction(event -> logIn());
        if(catalogoModel.getSelectedAuto() != null){
            guestButton.setDisable(true);
        }
    }

    @FXML
    protected void onGuestClick() {
        setPage("clienteView/homepageCliente");
    }

    @FXML
    private void goRegistrazione() {
        PageLoader.loadPage("/org/example/configuratoreauto/clienteView/registrazioneView.fxml", "Registrazione", (Stage) email.getScene().getWindow());
    }

    @FXML
    protected void logIn() {
        if (isInputValid.get()) {
            String emailText = email.getText();
            String passText = password.getText();

            //pulisco i campi di input
            email.clear();
            password.clear();
            Persona before = userModel.getCurrentUser();

            //Effettua una validazione sui dati ricevuti
            if (userModel.validation(emailText, passText)) {
                handleSuccessfulLogin(before);
            } else {
                showError("Email o Password errate");
            }
        }
    }

    private void handleSuccessfulLogin(Persona before) {
        if (userModel.isCliente()) {
            if (before != null) {
                catalogoModel.setSelectedAuto(null);
            }
            if(!guestButton.isDisabled())
                setPage("clienteView/homepageCliente");
        } else if (userModel.isImpiegato()) {
            setPage("impiegatoView/homepageImpiegato");
        } else if (userModel.isSegretario()) {
            setPage("segretarioView/homepageSegretario");
        }
        try {
            ((Stage) email.getScene().getWindow()).close();
        }catch(NullPointerException e){}
    }
    private void showError(String message) {
        responseText.setTextFill(Color.RED);
        responseText.setText(message);
    }
}
