package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

public class AutoController {
    @FXML
    private ImageView autoImage;
    @FXML
    private ImageView dynamicIcon;
    @FXML
    private Label NomeModello;
    @FXML
    private Label Marca;
    @FXML
    private Label Prezzo;

    UserModel utenti = UserModel.getInstance();
    CatalogoModel catalogo = CatalogoModel.getInstance();
    private AutoNuova auto;

    public void setAuto(AutoNuova auto) {
        this.auto = auto;
        NomeModello.setText(auto.getModello());
        Marca.setText(auto.getMarca().toString());
        Prezzo.setText(auto.getBasePriceAsString());
        // Carica l'immagine e imposta sull'ImageView
        Image image = new Image(getClass().getResourceAsStream("/img/test.png"));
        autoImage.setImage(image);
        String iconPath = (utenti.getCurrentUser() instanceof Segretario) ? "/img/icons/edit-icon.png" : "/img/icons/right-arrow.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        dynamicIcon.setImage(icon);

    }
    //Funzione che permette la gestione del click sulla componente
    @FXML
    public void handleHBoxClicked(){
        catalogo.setSelectedAuto(auto);
        //Controllo sul tipo di utente che ha cliccato
        if(utenti.getCurrentUser() instanceof Segretario){
            //Pagina modifica auto
        }
        else{
            //Apertura pagina
        }
        System.out.println(catalogo.getSelectedAuto());
    }
}
