package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

public class CatalogoController implements Initializable {

    @FXML
    private VBox autoList;

    CatalogoModel catalogo = CatalogoModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCars();
    }

    private void loadCars() {
        for (AutoNuova auto : catalogo.getAllData()) {
            System.out.println(catalogo.getAllData().size());
            try {
                loadCarComponent(auto);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCarComponent(AutoNuova a) throws IOException {
        // Carica la componente autoComponent.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/autoComponent.fxml"));
        HBox autoComponent = loader.load();

        // Configura il controller dell'autoComponent con i dati dell'auto
        AutoController controller = loader.getController();
        controller.setAuto(a);

        // Aggiungi l'autoComponent al HBox
        autoList.getChildren().add(autoComponent);
    }

    private void setFilter(){

    }
}