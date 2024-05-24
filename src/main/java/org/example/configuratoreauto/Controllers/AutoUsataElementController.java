package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
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
                TabPane tabPane = (TabPane) validation.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
                Tab preventivoTab = tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/impiegatoView/preventivoView.fxml"));
                BorderPane preventivoNode = loader.load();
                PreventivoDetailsController controller = loader.getController();
                controller.setPreventivo(preventivo);
                preventivoTab.setContent(preventivoNode); // Imposta il nuovo contenuto del tab "Catalogo"

            }catch (IOException e){
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            valutazione.setText("");
            validation.setText("Errore nell'inserimento della valutazione");

        }
    }
}
