package org.example.configuratoreauto.Controllers;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class ClienteController {
    UserModel userModel = UserModel.getInstance();
    CatalogoModel catalogo = CatalogoModel.getInstance();
    Cliente currentUser = (Cliente) userModel.getCurrentUser();

    //Nodi nella quale viene salvata la view della pagina desiderata
    AnchorPane catalogoNode;
    BorderPane preventiviNode;

    AnchorPane guestNode;

    @FXML
    private Label responseText;
    @FXML
    private  Label userName;
    @FXML
    private Pane mainPage;
    @FXML
    AnchorPane catalogoComponent;
    @FXML
    BorderPane preventiviComponent;


    //Setting degli event handlers, la funzione viene eseguita quando viene caricata la relativa pagina FXML
    @FXML
    private void initialize() throws InterruptedException {
        try {
            /*
                Carico all'interno dei nodi la rispettiva view FXML, definendo inoltre i relativi controller
            * */
            //CATALOGO
            FXMLLoader catalogoLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/catalogoView.fxml"));
            catalogoNode = catalogoLoader.load();
            CatalogoController catalogoController = catalogoLoader.getController();

            //PREVENTIVI
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            PreventiviController preventiviController = preventiviLoader.getController();

            FXMLLoader guestLoader =  new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/guestRegister.fxml"));
            guestNode = guestLoader.load();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //imposto come pagine predefinita il catalogo
        onCatalogo();
        setUserName();
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
    /*
    *   Azione avviata dal click del menù Catalogo:
    *       cancella l'elemento presente nella pagina, a cui sostituisce l'elemento CATALOGO
    * */
    @FXML
    private void onCatalogo(){
        mainPage.getChildren().clear();
        mainPage.getChildren().add(catalogoNode);
    }

    /*
     *   Azione avviata dal click del menù Preventivi:
     *       cancella l'elemento presente nella pagina, a cui sostituisce l'elemento PREVENTIVI
     * */
    @FXML
    private void onPreventivo(){
        mainPage.getChildren().clear();
        if(currentUser == null){
            mainPage.getChildren().add(guestNode);
        }else{
            mainPage.getChildren().add(preventiviNode);
        }
    }

}