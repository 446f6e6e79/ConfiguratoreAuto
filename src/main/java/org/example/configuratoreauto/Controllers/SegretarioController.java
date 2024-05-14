package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;
import java.io.IOException;

public class SegretarioController {

    //Nodi nella quale viene salvata la view della pagina desiderata
    AnchorPane catalogoNode;
    BorderPane preventiviNode;
    PreventiviController preventiviController;
    AnchorPane guestNode;

    @FXML
    private  Label userName;
    @FXML
    private Pane mainPage;

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

            //Setting dinamico delle dimensioni del catalogo
            catalogoNode.prefWidthProperty().bind(mainPage.widthProperty());
            catalogoNode.prefHeightProperty().bind(mainPage.heightProperty());

            //PREVENTIVI
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            //Setting dinamico delle dimensioni della pagina preventivi
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
            preventiviController = preventiviLoader.getController();

            FXMLLoader guestLoader =  new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/guestRegister.fxml"));
            guestNode = guestLoader.load();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //imposto come pagine predefinita il catalogo
        onCatalogo();
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
        mainPage.getChildren().add(preventiviNode);
        preventiviController.getPreventiviForCliente();
    }
}