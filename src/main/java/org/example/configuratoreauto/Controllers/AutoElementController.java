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
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

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

    private FXMLLoader loader; // Variabile per memorizzare il caricatore

    UserModel utenti = UserModel.getInstance();
    CatalogoModel catalogo = CatalogoModel.getInstance();
    private AutoNuova auto;

    public void setAuto(AutoNuova auto) {
        this.auto = auto;
        NomeModello.setText(auto.getModello());
        Marca.setText(auto.getMarca().toString());
        Prezzo.setText(auto.getBasePriceAsString());
        // Carica l'immagine e imposta sull'ImageView
        if(auto.getImmagini().size()!= 0){
            autoImage.setImage(auto.getImmagini().get(0).getImage());
        }
        else{
            Image image = new Image(getClass().getResourceAsStream("/img/test.png"));
            autoImage.setImage(image);
        }
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
            //Pagina modifica aut
            try{
                TabPane tabPane = (TabPane) autoImage.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
                Tab catalogoTab = tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/segretarioView/addModel.fxml"));
                AnchorPane autoeditor;

                autoeditor = loader.load();
                catalogoTab.setContent(autoeditor); // Imposta il nuovo contenuto del tab "Catalogo"
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        else{
            //Apertura pagina
            try {
                TabPane tabPane = (TabPane) autoImage.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
                Tab catalogoTab = tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/autoCustom.fxml"));
                AnchorPane autocustomNode;

                autocustomNode = loader.load();
                catalogoTab.setContent(autocustomNode); // Imposta il nuovo contenuto del tab "Catalogo"

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
