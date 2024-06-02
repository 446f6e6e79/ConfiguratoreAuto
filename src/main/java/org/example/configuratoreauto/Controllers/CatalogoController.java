package org.example.configuratoreauto.Controllers;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
    @FXML
    private TextField minPrice;
    @FXML
    private TextField maxPrice;


    private CatalogoModel catalogo = CatalogoModel.getInstance();
    private UserModel user = UserModel.getInstance();
    private ArrayList<AutoNuova> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoList.prefWidthProperty().bind(catalogoComponent.widthProperty());
        autoList.prefHeightProperty().bind(catalogoComponent.heightProperty());

        //Aggiungo i valori alle choiceBox usate per i filtri
        brandList.getItems().addAll(catalogo.getUsedBrands());
        alimentazioneList.getItems().addAll(Alimentazione.values());

        //Listener per l'implementazione dei filtri
        brandList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());
        alimentazioneList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());
//        minPrice.textProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());
//        maxPrice.textProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());

        PauseTransition pause = new PauseTransition(Duration.millis(1000)); // Adjust the delay as needed
        pause.setOnFinished(event -> filterAndLoadCars());

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            pause.playFromStart();
        };

        minPrice.textProperty().addListener(listener);
        maxPrice.textProperty().addListener(listener);

        minPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                filterAndLoadCars();
            }
        });

        maxPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                filterAndLoadCars();
            }
        });
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

        if (!minPrice.getText().isEmpty() || !maxPrice.getText().isEmpty()) {
            filteredList = CatalogoModel.filterAutoByPrice(minPrice.getText(), maxPrice.getText(), filteredList);
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
                catalogo.setSelectedAuto(null);
                TabPane tabPane = (TabPane) autoList.getScene().lookup("#mainPage");
                PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addModel.fxml");
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
        minPrice.clear();
        maxPrice.clear();
    }
}
