package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;


public class ClienteHomeController {
    UserModel userModel = UserModel.getInstance();

    RegistroModel registro = RegistroModel.getInstance();
    Cliente currentUser = (Cliente) userModel.getCurrentUser();

    BorderPane preventiviNode;
    PreventiviController preventiviController;
    @FXML
    private Label userName;
    @FXML
    private TabPane mainPage;
    @FXML
    private Tab catalogoTab;
    @FXML
    private Tab logout;
    @FXML
    private Tab preventiviTab;
    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    public void initialize() throws InterruptedException {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        mainPage.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.equals(logout)) {
                // Se clicca il tab logout richiamo il suo metodo
                mainPage.getSelectionModel().select(oldTab);//Mantengo il tab corrente
                logout();
            }
        });
        if(currentUser==null){
            logout.setText("Accedi");
            logout.setStyle("-fx-background-color: lightgreen;");
        }
        try{
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            //Setting dinamico delle dimensioni della pagina preventivi
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
            preventiviTab.setContent(preventiviNode);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        //Se è ospite
        if(userModel.getCurrentUser() == null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/loginPage.fxml"));
                VBox loginPane = loader.load();
                Stage stage = (Stage) mainPage.getScene().getWindow();

                Scene scene = new Scene(loginPane);
                stage.setScene(scene);
                stage.setTitle("Login");

                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Create a confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Sei sicuro di volerti disconnettere?");


        // Customize the buttons in the dialog
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                userModel = UserModel.getInstance();
                userModel.clearCurrentUser();
                logout.setText("Logout");
                // User clicked OK, proceed with logout
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/loginPage.fxml"));
                    VBox loginPane = loader.load();
                    Stage stage = (Stage) mainPage.getScene().getWindow();

                    Scene scene = new Scene(loginPane);
                    stage.setScene(scene);
                    stage.setTitle("Login");

                    stage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
        Questo metodo si occupa si gestire la label di saluto ottenendo i dati dal login precedente,
         inoltre nel caso ci fosse un accesso come ospite mostra la possibilità di registrazione
     */
    private void setUserName(){
        if (currentUser == null) {
            //BOTTONE CON ACCEDI??
            userName.setText("Benvenuto Ospite");
        }else{
            userName.setText("Benvenuto " + currentUser.getName());
        }
    }


}