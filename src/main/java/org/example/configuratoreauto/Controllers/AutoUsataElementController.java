package org.example.configuratoreauto.Controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Immagine;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import java.util.ArrayList;

public class AutoUsataElementController {

    private final IntegerProperty currentImageIndex = new SimpleIntegerProperty(0);
    RegistroModel registro = RegistroModel.getInstance();
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
    ImageView images;
    ArrayList<Immagine> listaImmagini;

    void setElement( Preventivo prev){
        validation.setText("");
        this.preventivo = prev;
        AutoUsata auto = prev.getUsata();
        listaImmagini = auto.getImmagini();
        updateImage();
        modello.setText(auto.getModello());
        targa.setText(auto.getTarga());
        marca.setText(auto.getMarca().toString());
        km.setText(auto.getKm()+" km");
    }
    @FXML
    void conferma() {
        try {
            double valutazioneValue = Double.parseDouble(valutazione.getText());
            preventivo.setValutazione(valutazioneValue);
            registro.updateData(preventivo);
            TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage");
            PageLoader.updateTabContent(tabPane, 0,"/org/example/configuratoreauto/impiegatoView/valutazioniView.fxml");
            PageLoader.updateTabContent(tabPane, 1, "/org/example/configuratoreauto/preventiviView.fxml");
        } catch (NumberFormatException e) {
            PageLoader.showErrorPopup("Errore", "Hai inserito una valutazione non valida!!");
        }
    }
    /**
     * Aggiorna l'imageView, caricando la foto puntata da currentImageIndex
     */
    private void updateImage() {
        if (!listaImmagini.isEmpty()) {
            images.setImage(listaImmagini.get(currentImageIndex.get()).getImage());
        }
    }

    /**
     * Aggiorna l'indice in modo da mostrare l'immagine precedente. Si distinguono due casi:
     *  - se currentImageIndex == 0 -> viene impostato all'ultima foto
     *  - altrimenti viene decrementato di 1
     */
    public void prevImage() {
        if (currentImageIndex.get() > 0) {
            currentImageIndex.set(currentImageIndex.get() - 1);
        } else {
            currentImageIndex.set(listaImmagini.size() - 1);
        }
        updateImage();
    }

    /**
     * Aggiorna l'indice in modo da mostrare l'immagine successiva. Si distinguono due casi:
     *  - se currentImageIndex == size - 1 -> viene impostato alla prima foto
     *  - altrimenti viene incrementato di 1
     */
    public void nextImage() {
        if (currentImageIndex.get() < listaImmagini.size() - 1) {
            currentImageIndex.set(currentImageIndex.get() + 1); // Increment index to move to the next image
        } else {
            currentImageIndex.setValue(0); // Wrap around to the first image
        }
        updateImage();
    }
}
