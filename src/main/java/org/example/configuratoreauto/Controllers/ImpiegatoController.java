package org.example.configuratoreauto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.example.configuratoreauto.Preventivi.Sede;
import org.example.configuratoreauto.Preventivi.SediModel;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

public class ImpiegatoController {
    UserModel userModel = UserModel.getInstance();
    SediModel sediModel = SediModel.getInstance();
    BorderPane preventiviNode;
    PreventiviController preventiviController;

    @FXML
    Pane mainPage;
    @FXML
    ChoiceBox<Sede> choiceSede;
    @FXML
    Label userName;

    @FXML
    private void initialize() {
        //Imposto le principali componenti della pagina
        //imposta un convertitore da Sede a stringa da permettere così una leggibilità per l'utente migliore, ed un codice più compatto
        choiceSede.setConverter(new SedeStringConverter());
        //inizializzo la choiceBox
        choiceSede.getItems().addAll(sediModel.getAllData());
        //imposto la Label dell'username
        userName.setText("Ciao Impiegato");
        try {
            //PREVENTIVI: carico la componente già utilizzata nella view del Cliente ( componente generalizzata )
            //            ed inoltre carico l'annesso controller utile per differenziare cliente ed impiegato
            FXMLLoader preventiviLoader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/preventiviView.fxml"));
            preventiviNode = preventiviLoader.load();
            preventiviController = preventiviLoader.getController();
        }catch(IOException e){
            e.printStackTrace();
        }
        //Al cambio della sede imposto i preventivi
        choiceSede.setOnAction(this::onPreventivo);
    }

    @FXML
    private void onPreventivo(ActionEvent event){
        //pulisco dalle componenti precedenti ed aggiorno a quelle recenti
        mainPage.getChildren().clear();
        mainPage.getChildren().add(preventiviNode);
        preventiviController.getPreventiviForImpiegato(choiceSede.getValue());
    }

    // classe utile alla conversione da Sede a String
    class SedeStringConverter extends javafx.util.StringConverter<Sede> {
        @Override
        public String toString(Sede sede) {
            if (sede != null) {
                return sede.getNome();
            } else {
                return "";
            }
        }

        @Override
        public Sede fromString(String string) {
            // Non necessario per questa situazione
            return null;
        }
    }
}