package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.configuratoreauto.Macchine.AutoNuova;

public class AutoController {
    @FXML
    private ImageView autoImage;
    @FXML
    private Label NomeModello;
    @FXML
    private Label Marca;
    @FXML
    private Label Prezzo;


    public void setAuto(AutoNuova auto) {
        NomeModello.setText(auto.getModello());
        Marca.setText(auto.getMarca().toString());
        Prezzo.setText(auto.getPriceAsString());
        // Carica l'immagine e imposta sull'ImageView
        Image image = new Image(getClass().getResourceAsStream("/img/test.png"));
        autoImage.setImage(image);
    }
}
