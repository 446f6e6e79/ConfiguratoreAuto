package org.example.configuratoreauto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.Sede;
import org.example.configuratoreauto.Preventivi.SediModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AutoCustomController implements Initializable {
    CatalogoModel catalogo = CatalogoModel.getInstance();
    SediModel sediModel = SediModel.getInstance();
    AutoNuova auto = catalogo.getSelectedAuto();
    RegistroModel registro = RegistroModel.getInstance();
    UserModel user = UserModel.getInstance();

    @FXML
    private AnchorPane main;
    @FXML
    private VBox left;
    @FXML
    private VBox right;
    @FXML
    private BorderPane borderPane;

    @FXML
    Text modelID;
    @FXML
    Text descrizione;
    @FXML
    private ImageView images;
    @FXML
    Text prezzo;
    @FXML
    Text infoPrezzo;
    @FXML
    ChoiceBox<Motore> motori;
    @FXML
    ChoiceBox<Optional> colori;
    @FXML
    ChoiceBox<Optional> interni;
    @FXML
    ChoiceBox<Optional> vetri;
    @FXML
    ChoiceBox<Optional> cerchi;
    @FXML
    Text motoreInfo;
    @FXML
    ChoiceBox<Sede> sedi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //============RESIZING
        borderPane.prefWidthProperty().bind(main.widthProperty());
        borderPane.prefHeightProperty().bind(main.heightProperty());
        left.prefWidthProperty().bind(borderPane.widthProperty().divide(2));
        right.prefWidthProperty().bind(borderPane.widthProperty().divide(2));
        images.fitWidthProperty().bind(left.prefWidthProperty());
        //================================

        modelID.setText(auto.getModello());
        descrizione.setText(auto.getDescrizione());
        prezzo.setText(auto.getBasePriceAsString());
        //Vorrey fare display solo dell'alimentazione come scelta
        motori.getItems().addAll(auto.getMotoriDisponibili());
        updatePriceInfo(new ArrayList<>());

        /*
            Listener sul cambio valore della choiceBox. Al cambiamento
            viene aggiornato il contenuto di motoreInfo
         */
        motori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                motoreInfo.setText(newValue.getInfoMotore());
            }
        });
        motori.setValue(motori.getItems().get(0));
        colori.getItems().addAll(auto.getOptionalByCategory(TipoOptional.colore));
        interni.getItems().addAll(auto.getOptionalByCategory(TipoOptional.interni));
        vetri.getItems().addAll(auto.getOptionalByCategory(TipoOptional.vetri));
        cerchi.getItems().addAll(auto.getOptionalByCategory(TipoOptional.cerchi));
        sedi.getItems().addAll(sediModel.getAllData());

        /*
        *   Aggiungo un listener a tutte le choice box contenente degli optional
        *   (colori, interni, vetri, cerchi). In questo modo possiamo aggiornare dinamicamente il prezzo
        * */
        ChangeListener<Optional> priceUpdateListener = (observable, oldValue, newValue) -> getDynamicPrice();
        colori.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        interni.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        vetri.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        cerchi.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
    }

    public void resetChoices(){
        colori.setValue(null);
        interni.setValue(null);
        vetri.setValue(null);
        cerchi.setValue(null);
        sedi.setValue(null);
    }
    public void createPreventivo() {
        Preventivo p = new Preventivo(auto, sedi.getValue(), (Cliente) user.getCurrentUser(), motori.getValue(), colori.getValue());
        registro.addData(p);
        //Gestione su cosa fare, dopo aver aggiunto il preventivo
    }

    public void getDynamicPrice() {
        ArrayList<Optional> selezionati = new ArrayList<>();
        selezionati.add(colori.getValue());
        selezionati.add(interni.getValue());
        selezionati.add(vetri.getValue());
        selezionati.add(cerchi.getValue());
        double costoTotale =auto.getCostoTotale(selezionati);
        updatePriceInfo(selezionati);
        prezzo.setText(AutoNuova.getPriceAsString(costoTotale));
    }

    public void updatePriceInfo(ArrayList<Optional> selezionati) {
        String s= "Costo base: "+auto.getBasePriceAsString();
        for(Optional o : selezionati) {
            s+= o.toString() + "\n";
        }
        //Aggiungere un metodo per mostrare sconto, se presente
        infoPrezzo.setText(s);
    }
    @FXML
    public void backClicked(){
        try {
            TabPane tabPane = (TabPane) modelID.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
            AnchorPane catalogoNode;

            catalogoNode = loader.load();
            tab.setContent(catalogoNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
