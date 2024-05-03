package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

public class CatalogoController implements Initializable {

    @FXML
    private FlowPane autoContainer;

    CatalogoModel catalogo = CatalogoModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCars();
    }

    private void loadCars() {
        for (AutoNuova auto : catalogo.getAllData()) {
            try {
                // Carica la componente autoComponent.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/autoComponent.fxml"));
                VBox autoComponent = loader.load();
                // Configura il controller dell'autoComponent con i dati dell'auto
                AutoController controller = loader.getController();
                controller.setAuto(auto);

                // Aggiungi l'autoComponent al FlowPane
                autoContainer.getChildren().add(autoComponent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setFilter(){

    }
}
