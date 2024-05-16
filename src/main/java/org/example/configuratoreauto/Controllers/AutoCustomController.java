package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;

import java.net.URL;
import java.util.ResourceBundle;

public class AutoCustomController implements Initializable {
    CatalogoModel catalogo = CatalogoModel.getInstance();
    AutoNuova auto = catalogo.getSelectedAuto();

    @FXML
    Text modelID;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelID.setText(auto.getModello());
    }
}
