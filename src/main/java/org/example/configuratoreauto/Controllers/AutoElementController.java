package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.UserModel;

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

    UserModel userModel = UserModel.getInstance();
    CatalogoModel catalogoModel = CatalogoModel.getInstance();
    private AutoNuova auto;

    public void setAuto(AutoNuova auto) {
        this.auto = auto;
        NomeModello.setText(auto.getModello());
        Marca.setText(auto.getMarca().toString());
        Prezzo.setText(auto.getBasePriceAsString());
        /*
         * TODO: una volta aggiunti i colori ad ogni auto, l'if può essere rimosso
         * */
        if(auto.getDefaultColor() == null){
            autoImage.setImage(auto.getDefaultImage(null));
        }else{
            autoImage.setImage(auto.getDefaultImage(auto.getDefaultColor().getDescrizione()));

        }

        String iconPath = (userModel.isSegretario()) ? "/img/icons/edit-icon.png" : "/img/icons/right-arrow.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        dynamicIcon.setImage(icon);

    }

    //Funzione che permette la gestione del click sulla componente
    @FXML
    public void handleHBoxClicked(){
        catalogoModel.setSelectedAuto(auto);
        TabPane tabPane = (TabPane) autoImage.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
        //Controllo sul tipo di utente che ha cliccato
        if(userModel.isSegretario()){
            //Pagina modifica auto impostata
            PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addModel.fxml" );
        }
        else{
            //Apertura pagina personalizzazione e richeista auto
            PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/clienteView/autoCustomization.fxml" );
        }
    }
}
