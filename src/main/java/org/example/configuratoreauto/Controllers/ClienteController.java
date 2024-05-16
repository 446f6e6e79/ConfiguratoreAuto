package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;
import javafx.scene.control.TabPane;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;


public class ClienteController {
    UserModel userModel = UserModel.getInstance();
    Cliente currentUser = (Cliente) userModel.getCurrentUser();

    BorderPane preventiviNode;
    PreventiviController preventiviController;
    @FXML
    private  Label userName;
    @FXML
    private TabPane mainPage;
    @FXML
    private Tab catalogoTab;
    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() throws InterruptedException {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        try{
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            //Setting dinamico delle dimensioni della pagina preventivi
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
            preventiviController = preventiviLoader.getController();
            preventiviController.getPreventiviForCliente();
            System.out.println("Resettato main");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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