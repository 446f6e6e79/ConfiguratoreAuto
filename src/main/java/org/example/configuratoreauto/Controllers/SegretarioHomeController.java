package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.TabPane;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.SediModel;

import java.io.IOException;


public class SegretarioHomeController {

    RegistroModel registro = RegistroModel.getInstance();
    BorderPane preventiviNode;
    PreventiviController preventiviController;
    @FXML
    private TabPane mainPage;

    @FXML
    private void initialize() throws InterruptedException {
        mainPage.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        try{
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            //Setting dinamico delle dimensioni della pagina preventivi
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
            preventiviController = preventiviLoader.getController();
            //GET 0 MOMENTANEO
            preventiviController.loadPrevs(registro.getPreventiviBySede(SediModel.getInstance().getAllData().get(0)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}