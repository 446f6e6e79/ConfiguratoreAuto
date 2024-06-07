package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SegretarioHomeController extends AbstractHomeController {
    BorderPane preventiviNode;

    @FXML
    @Override
    protected void initialize(){
        super.initialize();

        try {
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void customizeLogoutTab() {

    }
}
