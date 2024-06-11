package org.example.configuratoreauto.Controllers;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.example.configuratoreauto.Controllers.InputValidation.checkValidInt;

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
    @FXML
    private ToggleButton orderCrescente;
    @FXML
    private ToggleButton orderDecrescente;

    private CatalogoModel catalogoModel = CatalogoModel.getInstance();
    private UserModel userModel = UserModel.getInstance();
    private ArrayList<AutoNuova> currentFilteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoList.prefWidthProperty().bind(catalogoComponent.widthProperty());
        autoList.prefHeightProperty().bind(catalogoComponent.heightProperty());

        //Aggiungo i valori alle choiceBox usate per i filtri
        brandList.getItems().addAll(catalogoModel.getUsedBrands());
        alimentazioneList.getItems().addAll(Alimentazione.values());

        //Listener per l'implementazione dei filtri
        brandList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());
        alimentazioneList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterAndLoadCars());

        //Permetto solamente input interi nei campi prezzoMin e prezzoMax dei filtri
        minPrice.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidInt(event, minPrice));
        maxPrice.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidInt(event, maxPrice));

        //Impostazione per il filtro del prezzo
        PauseTransition pause = new PauseTransition(Duration.millis(1000));
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

        //Imposto il comportamento di orderCrescente
        orderCrescente.selectedProperty().addListener((observable, oldValue, newValue) -> {
            //Se è stato selezionato, ordino la lista in modo crescente
            if (newValue) {
                orderDecrescente.setSelected(false);
                sortAndLoadCars(true);
            }
            else{
                orderCrescente.setSelected(false);
                filterAndLoadCars();
            }
        });

        //Imposto il comportamento di orderDecrescente
        orderDecrescente.selectedProperty().addListener((observable, oldValue, newValue) -> {
            //Se è stato selezionato, ordino la lista in modo decrescente
            if (newValue) {
                //Deseleziono l'altra opzione
                orderCrescente.setSelected(false);
                //Ordino la lista di auto
                sortAndLoadCars(false);
            }
            else{
                orderDecrescente.setSelected(false);
                filterAndLoadCars();
            }
        });


        filterAndLoadCars();
    }

    private void filterAndLoadCars() {
        double minPriceValue = minPrice.getText().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPrice.getText());
        double maxPriceValue = maxPrice.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPrice.getText());
        //Filtro la lista di auto
        currentFilteredList = catalogoModel.
                filterAuto(brandList.getValue(),
                        alimentazioneList.getValue(),
                        minPriceValue,
                        maxPriceValue);
        //Se ho selezionato un'ordinamento, ordino la lista filtrata
        if (orderCrescente.isSelected() || orderDecrescente.isSelected()) {
            sortAndLoadCars(orderCrescente.isSelected());
        }
        else{
            loadCars();
        }
    }


    private void sortAndLoadCars(boolean crescente) {
        Comparator<AutoNuova> c = AutoNuova.getComparatorByPrice();
        if (!crescente) {
            c = c.reversed();
        }
        currentFilteredList.sort(c);
        loadCars();
    }


    private void loadCars() {
        autoList.getChildren().clear();

        if (currentFilteredList.isEmpty()) {
            autoList.getChildren().add(new Text("Non è presente alcuna auto che rispetta i seguenti filtri"));
        }

        if (userModel.isSegretario()) {
            addInsertAutoElement();
        }

        for (AutoNuova auto : currentFilteredList) {
            try {
                loadCarComponent(auto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Aggiunge un elemento cliccabile in cima alla lista. Tale
     * elemnto, una volta cliccato permette l'aggiunta di un
     * nuovo modello alla
     */
    private void addInsertAutoElement() {
        HBox imageContainer = new HBox();
        imageContainer.setPrefSize(542, 240);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getStyleClass().add("clickableElement");

        imageContainer.setOnMouseClicked(event -> {
                catalogoModel.setSelectedAuto(null);
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

    /**
     * Carica il componente per la singola auto all'interno del catalogo
     *
     * @param autoToDisplay AutoNuova, da aggiungere al catalogo
     */
    private void loadCarComponent(AutoNuova autoToDisplay) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/autoElement.fxml"));
        HBox autoComponent = loader.load();
        AutoElementController controller = loader.getController();
        controller.setAuto(autoToDisplay);
        autoList.getChildren().add(autoComponent);
    }

    /**
     * Resetta i filtri e gli ordinamenti selezionati
     */
    @FXML
    private void resetFilters() {
        brandList.setValue(null);
        alimentazioneList.setValue(null);
        minPrice.clear();
        maxPrice.clear();
        orderCrescente.setSelected(false);
        orderDecrescente.setSelected(false);
    }
}
