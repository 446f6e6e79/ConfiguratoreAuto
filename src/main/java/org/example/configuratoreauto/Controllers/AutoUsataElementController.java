package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;

import java.awt.*;
import java.io.IOException;

public class AutoUsataElementController {

    RegistroModel registo = RegistroModel.getInstance();
    Preventivo preventivo;
    @FXML
    Label modello;
    @FXML
    Label marca;
    @FXML
    Label km;
    @FXML
    Label targa;
    @FXML
    TextField valutazione;
    @FXML
    private Label validation;
    @FXML
    ImageView autoimage;
    void setElement( Preventivo prev){
        validation.setText("");
        this.preventivo = prev;
        AutoUsata auto = prev.getUsata();
        modello.setText(auto.getModello());
        targa.setText(auto.getTarga());
        marca.setText(auto.getMarca().toString());
        km.setText(auto.getKm()+"");
    }
    @FXML
    void conferma() {
        try {
            double valutazioneValue = Double.parseDouble(valutazione.getText());
            preventivo.setValutazione(valutazioneValue);
            //Apertura pagina
            try {
                TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
                Tab valutazioneTab = tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/impiegatoView/valutazioniView.fxml"));
                BorderPane valutazione = loader.load();
                valutazioneTab.setContent(valutazione);
            }catch (IOException e){
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            valutazione.setText("");
            validation.setText("Errore nell'inserimento della valutazione");

        }
    }
}
