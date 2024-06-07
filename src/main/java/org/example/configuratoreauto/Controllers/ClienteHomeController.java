package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import org.example.configuratoreauto.Utenti.Cliente;

import java.io.IOException;

public class ClienteHomeController extends AbstractHomeController {
    Cliente currentUser = (Cliente) userModel.getCurrentUser();

    BorderPane preventiviNode;

    @FXML
    private Tab preventiviTab;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        try {
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            preventiviNode.prefWidthProperty().bind(mainPage.widthProperty());
            preventiviNode.prefHeightProperty().bind(mainPage.heightProperty());
            preventiviTab.setContent(preventiviNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void customizeLogoutTab() {
        if (currentUser == null) {
            logout.setText("Accedi");
            logout.setStyle("-fx-background-color: lightgreen;");
        }else{
            logout.setText("Logout");
            logout.setStyle("-fx-background-color: ffcccc;");
        }
    }
}
