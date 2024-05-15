package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.TabPane;
import java.io.IOException;


public class SegretarioController {

    BorderPane preventiviNode;
    PreventiviController preventiviController;
    @FXML
    private TabPane mainPage;

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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}