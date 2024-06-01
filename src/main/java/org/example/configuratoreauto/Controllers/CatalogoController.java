package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CatalogoController implements Initializable {

    @FXML
    private BorderPane catalogoComponent;
    @FXML
    private FlowPane autoList;
    @FXML
    private ChoiceBox<Marca> brandList;
    @FXML
    private ChoiceBox<Alimentazione> alimentazioneList;

    private CatalogoModel catalogo = CatalogoModel.getInstance();
    private UserModel user = UserModel.getInstance();
    private ArrayList<AutoNuova> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoList.prefWidthProperty().bind(catalogoComponent.widthProperty());
        autoList.prefHeightProperty().bind(catalogoComponent.heightProperty());

        brandList.getItems().addAll(catalogo.getUsedBrands());
        alimentazioneList.getItems().addAll(CatalogoModel.getUsedAlimentazione(catalogo.getAllData()));

        brandList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());
        alimentazioneList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());

        filterAndLoadCars();
    }

    private void filterAndLoadCars() {
        filteredList = catalogo.getAllData();

        if (brandList.getValue() != null) {
            filteredList = CatalogoModel.filterAutoByBrand(brandList.getValue(), filteredList);
        }

        if (alimentazioneList.getValue() != null) {
            filteredList = CatalogoModel.filterAutoByAlimentazione(alimentazioneList.getValue(), filteredList);
        }

        loadCars();
    }

    private void loadCars() {
        autoList.getChildren().clear();

        if (filteredList.isEmpty()) {
            autoList.getChildren().add(new Text("Non è presente alcuna auto che rispetta i seguenti filtri"));
        }

        if (user.getCurrentUser() instanceof Segretario) {
            addInsertAutoElement();
        }

        for (AutoNuova auto : filteredList) {
            try {
                loadCarComponent(auto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addInsertAutoElement() {
        HBox imageContainer = new HBox();
        imageContainer.setPrefSize(542, 240);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getStyleClass().add("clickableElement");

        imageContainer.setOnMouseClicked(event -> {
            try {
                catalogo.setSelectedAuto(null);
                TabPane tabPane = (TabPane) autoList.getScene().lookup("#mainPage");
                Tab editCatalogoTab = tabPane.getTabs().get(0);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/segretarioView/addModel.fxml"));
                AnchorPane autocustomNode = loader.load();
                editCatalogoTab.setContent(autocustomNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ImageView inserisciAuto = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/img/icons/car.png"));
        inserisciAuto.setImage(image);
        inserisciAuto.setPreserveRatio(true);
        inserisciAuto.setFitWidth(image.getWidth() / 2);

        imageContainer.getChildren().add(inserisciAuto);
        autoList.getChildren().add(imageContainer);
    }

    private void loadCarComponent(AutoNuova a) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/autoElement.fxml"));
        HBox autoComponent = loader.load();

        AutoElementController controller = loader.getController();
        controller.setAuto(a);

        autoList.getChildren().add(autoComponent);
    }

    @FXML
    private void resetFilters() {
        brandList.setValue(null);
        alimentazioneList.setValue(null);
        filterAndLoadCars();
    }
}
