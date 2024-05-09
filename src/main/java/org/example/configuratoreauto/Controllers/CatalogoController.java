package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.Alimentazione;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Macchine.Marca;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CatalogoController implements Initializable {

    @FXML
    private VBox autoList;
    @FXML
    private ChoiceBox<Marca> brandList;
    @FXML
    private ChoiceBox<Alimentazione> alimentazioneList;


    CatalogoModel catalogo = CatalogoModel.getInstance();

    private ArrayList<AutoNuova> filteredList = catalogo.getAllData();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Aggiungo le auto alla VBox
        loadCars();
        loadBrandFilter();
        loadAlimentazioni();

        /*
        *   Aggiungo u
        * */
        brandList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setFilter();
            loadCars(); // Reload cars when brand selection changes
        });

        alimentazioneList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setFilter();
            loadCars(); // Reload cars when alimentazione selection changes
        });
    }
    /*
    *   Verranno mostrati solamente i brand presenti all'interno del catalogo
    * */
    private void loadBrandFilter() {
        for(Marca m:catalogo.getUsedBrands()){
            brandList.getItems().add(m);
        }
    }
    private void loadAlimentazioni() {
        for(Alimentazione a:Alimentazione.values()){
            alimentazioneList.getItems().add(a);
        }
    }
    private void loadCars() {
        //Resetto la lista presente in precedenza
        autoList.getChildren().clear();
        if(filteredList.isEmpty()){
            autoList.getChildren().add(new Text("Non è presente alcuna auto che rispetta i seguenti filtri"));
        }
        for (AutoNuova auto : filteredList) {
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

        // Aggiungi l'autoComponent al VBox
        autoList.getChildren().add(autoComponent);
    }

    private void setFilter(){
        //Ogni qual volta applico un nuovo filtro, devo rigenerare l'intera lista di auto
        this.filteredList = catalogo.getAllData();

        if(brandList.getSelectionModel().getSelectedItem() != null) {
            this.filteredList = catalogo.filterAutoByBrand(brandList.getSelectionModel().getSelectedItem(), this.filteredList);
        }
        if(alimentazioneList.getSelectionModel().getSelectedItem() != null) {
            this.filteredList = catalogo.filterAutoByAlimentazione(alimentazioneList.getSelectionModel().getSelectedItem(), this.filteredList);
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
    }
}