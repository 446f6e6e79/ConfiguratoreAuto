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
    private Label modelName;

    public void setAuto(AutoNuova auto) {
        modelName.setText(auto.getModello());
        // Carica l'immagine e imposta sull'ImageView
        Image image = new Image(getClass().getResourceAsStream("/img/test.jpeg"));
        autoImage.setImage(image);
    }
}
