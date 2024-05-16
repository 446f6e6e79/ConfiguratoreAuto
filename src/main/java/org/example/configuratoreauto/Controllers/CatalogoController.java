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
import org.example.configuratoreauto.Macchine.Alimentazione;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CatalogoController implements Initializable {

    @FXML
    private AnchorPane catalogoComponent;
    @FXML
    private FlowPane autoList;
    @FXML
    private ChoiceBox<Marca> brandList;
    @FXML
    private ChoiceBox<Alimentazione> alimentazioneList;

    CatalogoModel catalogo = CatalogoModel.getInstance();
    UserModel user = UserModel.getInstance();
    private ArrayList<AutoNuova> filteredList = catalogo.getAllData();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        autoList.prefWidthProperty().bind(catalogoComponent.widthProperty());
        autoList.prefHeightProperty().bind(catalogoComponent.heightProperty());

        //Carico le auto

        loadCars();

        //Carico gli elementi per i filtri
        loadBrandFilter();
        loadAlimentazioni();

        /*
        *   Aggiungo dei listener alle varie CHOICEBOX per i filtri. In questo modo, ogni volta
        *   viene impostato un nuovo filtro viene AGGIORNATA LA LISTA DI MACCHINE
        * */
        brandList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setFilter();
            loadCars();
            loadAlimentazioni();
        });
        alimentazioneList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setFilter();
            loadCars();
        });
    }
    /*
    *   Carico nella choiceBox brandList i possibili brand dispobili
    * */
    private void loadBrandFilter() {
        brandList.getItems().addAll(catalogo.getUsedBrands());
    }
    private void loadAlimentazioni() {
        System.out.println("Load Alimentazioni: "+CatalogoModel.getUsedAlimentazione(filteredList));
        alimentazioneList.getItems().clear();
        alimentazioneList.getItems().addAll(CatalogoModel.getUsedAlimentazione(filteredList));
    }
    private void loadCars() {
        //Resetto la lista presente in precedenza
        autoList.getChildren().clear();
        if(filteredList.isEmpty()){
            autoList.getChildren().add(new Text("Non è presente alcuna auto che rispetta i seguenti filtri"));
        }
        if(user.getCurrentUser() instanceof Segretario){
            addInsertAutoElement();
        }

        for (AutoNuova auto : filteredList) {
            try {
                loadCarComponent(auto);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    *   Se l'utente è di tipo SEGRETARIO, introduce, in cima alla lista di auto
    *   un nuovo elemento cliccabile, con la possibilità di aggiungere un nuovo modello di auto
    * */
    private void addInsertAutoElement() {
        HBox imageContainer = new HBox();
        imageContainer.setPrefSize(630, 300);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setStyle("-fx-border-color: BLACK; -fx-border-width: 2; -fx-border-radius: 10;");

        //Aggiungo un nuovo evento al click dell'elemento
        imageContainer.setOnMouseClicked(event -> {
            //Apro la pagina per l'aggiunta di una nuova auto
            try {
                TabPane tabPane = (TabPane) autoList.getScene().lookup("#mainPage");    //Ottiengo il riferimento al TabPane
                Tab editCatalogoTab = tabPane.getTabs().get(0);                                //Ottiengo il riferimento alla tab "Modifica Catalogo"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/segretarioView/addAuto.fxml"));
                AnchorPane autocustomNode;
                autocustomNode = loader.load();
                editCatalogoTab.setContent(autocustomNode); // Imposta il nuovo contenuto del tab "Catalogo"

            }catch (IOException e){
                e.printStackTrace();
            }
        });

        ImageView inserisciAuto = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/img/icons/car.png"));
        inserisciAuto.setImage(image);

        //Dimezzo le dimensioni dell'immagine
        inserisciAuto.setPreserveRatio(true);
        inserisciAuto.setFitWidth(image.getWidth() / 2);

        imageContainer.getChildren().add(inserisciAuto);
        autoList.getChildren().add(imageContainer);
    }

    private void loadCarComponent(AutoNuova a) throws IOException {
        // Carica la componente autoComponent.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/autoComponent.fxml"));
        HBox autoComponent = loader.load();

        // Configura il controller dell'autoComponent con i dati dell'auto
        AutoController controller = loader.getController();
        controller.setAuto(a);

        // Aggiungi l'autoComponent al VBox
        autoList.getChildren().add(autoComponent);
    }

    private void setFilter(){
        //Ogni qual volta applico un nuovo filtro, devo rigenerare l'intera lista di auto
        filteredList = catalogo.getAllData();

        if(brandList.getSelectionModel().getSelectedItem() != null) {
            filteredList = CatalogoModel.filterAutoByBrand(brandList.getSelectionModel().getSelectedItem(), filteredList);
        }
        if(alimentazioneList.getSelectionModel().getSelectedItem() != null) {
            filteredList = CatalogoModel.filterAutoByAlimentazione(alimentazioneList.getSelectionModel().getSelectedItem(), filteredList);
        }
    }
    /*
    *   Funzione che setta a NULL tutti i filtri presenti nella pagina.
    *   In questo modo possiamo ottenere nuovamente la lista completa di veicoli
    * */
    @FXML
    private void resetFilters(){
        brandList.setValue(null);
        alimentazioneList.setValue(null);
        loadBrandFilter();
    }
}