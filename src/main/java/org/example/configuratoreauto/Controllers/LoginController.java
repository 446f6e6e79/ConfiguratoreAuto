package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.*;

import java.io.IOException;

import static org.example.configuratoreauto.Main.setPage;

public class LoginController {
    UserModel userModel = UserModel.getInstance();
    CatalogoModel catalogo = CatalogoModel.getInstance();
    @FXML
    private Label responseText;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    private ReadOnlyBooleanWrapper isInputValid = new ReadOnlyBooleanWrapper(false);

    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() {

        /*Leghiamo il valore di isInputEmpty ai due campi di input
        *   - TRUE se entrambi i campi sono vuoti
        *   - FALSE se almeno uno due campi non è vuoto
        * */
        isInputValid.bind(email.textProperty().isNotEmpty().and(password.textProperty().isNotEmpty()));

        login.disableProperty().bind(isInputValid.not());


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
    private void goRegistrazione(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/registrazioneView.fxml"));
            VBox registrazioneView = loader.load();

            Stage stage = (Stage) email.getScene().getWindow();

            Scene scene = new Scene(registrazioneView);
            stage.setScene(scene);
            stage.setTitle("Registrazione");

            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void logIn() {
        if(isInputValid.get()) {
            String emailText = email.getText();
            String passText = password.getText();
            email.clear();
            password.clear();
            Persona before = userModel.getCurrentUser();
            if (userModel.validation(emailText, passText)) {
                System.out.println(userModel.getCurrentUser());
                if (userModel.getCurrentUser() instanceof Cliente ) {
                    if(before != null)
                        catalogo.setSelectedAuto(null);
                    setPage("clienteView/homepageCliente");
                }
                if (userModel.getCurrentUser() instanceof Impiegato) {
                    setPage("impiegatoView/homepageImpiegato");
                }
                if (userModel.getCurrentUser() instanceof Segretario) {
                    setPage("segretarioView/homepageSegretario");
                }
                try {
                    ((Stage) email.getScene().getWindow()).close();
                }catch(NullPointerException e){}
            } else {
                responseText.setTextFill(Color.RED);
                responseText.setText("Email o Password errate");
            }
        }
    }
}