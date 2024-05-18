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
    /*
    *   Definisco delle variabili  Observable che permettono di implementare il pattern observer:
    *       -currentIndex è un numero intero, indica la foto attualmente selezionata
    *       -currentImages è una ArrayList, contenente le immagini
    * */
    private IntegerProperty currentIndex = new SimpleIntegerProperty(-1);
    private ObservableList<Image> currentImages = FXCollections.observableArrayList();
    private ArrayList<Image> allImages = new ArrayList<>();

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
    @FXML
    private TextField basePrice;


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
                () -> currentIndex.get() < currentImages.size() - 1 && currentImages.size() > 1,
                currentIndex
        ));

        /*
        *   Setto visibile il tasto per eliminare la foto solamente nel momento in cui è
        *   presente almeno un immagine
        * */
        deletePhoto.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> !currentImages.isEmpty(),
                currentImages
        ));
        /*
        *   Setto le action per i 3 bottoni presenti nell'inserimento dell'immagine
        * */
        photoLeft.setOnMouseClicked(e -> getPreviousPhoto());
        photoRight.setOnMouseClicked(e -> getNextPhoto());
        deletePhoto.setOnMouseClicked(e -> deletePhoto());

        //Disabilito i bottoni per l'aggiunta di foto se non ho inserito il colore
        buttonBar.disableProperty().bind(colore.textProperty().isEmpty());
    }

    public void getNextPhoto(){
        if (currentIndex.get() < currentImages.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            addedImages.setImage(currentImages.get(currentIndex.get()));
        }
        System.out.println("currentIndex:"+currentIndex.get());
        System.out.println("SIZE:"+currentImages.size());
    }


    public void getPreviousPhoto(){
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
            addedImages.setImage(currentImages.get(currentIndex.get()));
        }
        System.out.println(currentIndex.get());
    }


    private void deletePhoto(){
        currentImages.remove(currentIndex.get());
        if (currentImages.isEmpty()) {
            currentIndex.set(-1);
            addedImages.setImage(null);
        } else {
            if (currentIndex.get() >= currentImages.size()) {
                currentIndex.set(currentImages.size() - 1);
            }
            addedImages.setImage(currentImages.get(currentIndex.get()));
        }
    }

    /*
    *   Salvo localmente le immagini per tale colore. Verranno caricate
    * */
    @FXML
    private void saveImages(){
        allImages.addAll(currentImages);
        currentImages.clear();
        currentIndex.set(-1);
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
            Image image = new Image(imageFile.toURI().toString());
            currentImages.add(image);
            currentIndex.set(currentImages.size()-1);
            System.out.println(currentIndex.get());
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
