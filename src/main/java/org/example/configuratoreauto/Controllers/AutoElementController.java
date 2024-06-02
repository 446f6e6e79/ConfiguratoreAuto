package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Macchine.TipoOptional;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.util.List;

public class AutoElementController {
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
        if(auto.getDefaultColor() == null){
            autoImage.setImage(auto.getDefaultImage(null));
        }else{
            autoImage.setImage(auto.getDefaultImage(auto.getDefaultColor().getDescrizione()));

        }

        String iconPath = (utenti.getCurrentUser() instanceof Segretario) ? "/img/icons/edit-icon.png" : "/img/icons/right-arrow.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        dynamicIcon.setImage(icon);

    }

    //Funzione che permette la gestione del click sulla componente
    @FXML
    public void handleHBoxClicked(){
        catalogo.setSelectedAuto(auto);
        TabPane tabPane = (TabPane) autoImage.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
        //Controllo sul tipo di utente che ha cliccato
        if(utenti.getCurrentUser() instanceof Segretario){
            //Pagina modifica auto impostata
            PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addModel.fxml" );
        }
        else{
            //Apertura pagina personalizzazione e richeista auto
            PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/clienteView/autoCustomization.fxml" );
        }
    }
}
