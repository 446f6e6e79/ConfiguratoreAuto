package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.configuratoreauto.Preventivi.RegistroModel;

import java.io.IOException;

public class UsataCustomController {

    RegistroModel registro = RegistroModel.getInstance();
    @FXML
    Text title;
    @FXML
    void saltaButton(){
        registro.addData(registro.currentPreventivo);
        ((Stage) title.getScene().getWindow()).close();
    }
}
