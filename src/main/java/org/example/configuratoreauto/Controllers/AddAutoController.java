package org.example.configuratoreauto.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAutoController implements Initializable {
    private IntegerProperty currentIndex = new SimpleIntegerProperty(-1);
    private ObservableList<Image> images = FXCollections.observableArrayList();


    @FXML
    private TextArea modello;
    @FXML
    private TextField descrizione;
    @FXML
    private ImageView addedImages;
    @FXML
    private ImageView deletePhoto;
    @FXML
    private ImageView photoRight;
    @FXML
    private ImageView photoLeft;
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private TextField colore;


    CatalogoModel catalogo = CatalogoModel.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
         *   Collego la visibilità dei bottoni al valore di currentIndex:
         *      -BottoneSX: mostrato solo se index > 0
         *      -BottoneDX: mostrato solo se index < size-1
         * */
        photoLeft.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> currentIndex.get() > 0,
                currentIndex
        ));
        photoRight.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> currentIndex.get() < images.size() - 1,
                currentIndex
        ));

        /*
        *   Setto visibile il tasto per eliminare la foto solamente nel momento in cui è
        *   presente almeno un immagine
        * */
        deletePhoto.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> !images.isEmpty(),
                images
        ));

        photoLeft.setOnMouseClicked(e -> getPreviousPhoto());

        photoRight.setOnMouseClicked(e -> getNextPhoto());

        deletePhoto.setOnMouseClicked(e -> {
            if (!images.isEmpty()) {
                images.remove(currentIndex.get());
                if (currentIndex.get() > 0) {
                    currentIndex.set(currentIndex.get() - 1);
                }
                if (!images.isEmpty()) {
                    addedImages.setImage(images.get(currentIndex.get()));
                } else {
                    addedImages.setImage(null);
                }
            }
        });
        //Disabilito i bottoni per l'aggiunta di foto se non ho inserito il colore
        buttonBar.disableProperty().bind(colore.textProperty().isEmpty());
    }

    public void getNextPhoto(){
        if (currentIndex.get() < images.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            addedImages.setImage(images.get(currentIndex.get()));
        }
    }

    public void getPreviousPhoto(){
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
            addedImages.setImage(images.get(currentIndex.get()));
        }
    }

    @FXML
    private void addImageFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png"),
                new FileChooser.ExtensionFilter("Image files", "*.jpg")
        );

        File imageFile = fileChooser.showOpenDialog(new Stage());
        //Gestione delle immagini
        if(imageFile != null){
            currentIndex.set(currentIndex.get() + 1);
            Image image = new Image(imageFile.toURI().toString());
            images.add(image);
            System.out.println(image.getUrl());
            addedImages.setImage(image);
        }
    }

    public void backClicked(){
        try {
            TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
            AnchorPane catalogoNode;

            catalogoNode = loader.load();
            tab.setContent(catalogoNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
