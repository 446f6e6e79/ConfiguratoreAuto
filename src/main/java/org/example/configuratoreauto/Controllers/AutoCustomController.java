package org.example.configuratoreauto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.Sede;
import org.example.configuratoreauto.Preventivi.SediModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

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
    Text modelID;
    @FXML
    Text descrizione;
    @FXML
    Text prezzo;
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
    Text motoriInfo;
    @FXML
    ChoiceBox<Sede> sedi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelID.setText(auto.getModello());
        descrizione.setText(auto.getDescrizione());
        prezzo.setText(auto.getBasePriceAsString());
        //Vorrey fare display solo dell'alimentazione come scelta
        motori.getItems().addAll(auto.getMotoriDisponibili());

        /*
            Listener sul cambio valore della choiceBox. Al cambiamento
            viene aggiornato il contenuto di motoreInfo
         */
        motori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                motoriInfo.setText(newValue.toString());
            }
        });
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
        prezzo.setText(auto.getPriceAsString(costoTotale));
    }

    public void updatePriceInfo(ArrayList<Optional> selezionati) {
        String s = "";
        for(Optional o : selezionati) {
            s+= o.toString() + "\n";
        }
        //Aggiungere un metodo per mostrare sconto, se presente
    }
}
