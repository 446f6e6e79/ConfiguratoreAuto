package org.example.configuratoreauto.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Macchine.TipoOptional;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAutoImagesController implements Initializable {
    /*
     *   Definisco delle variabili  Observable che permettono di implementare il pattern observer:
     *       -currentIndex è un numero intero, indica la foto attualmente selezionata
     *       -currentImages è una ArrayList, contenente le immagini
     * */
    private IntegerProperty currentIndex = new SimpleIntegerProperty(-1);
    private ObservableList<Image> currentImages = FXCollections.observableArrayList();
    private ArrayList<Image> allImages = new ArrayList<>();

    @FXML
    private ImageView addedImages;
    @FXML
    private ImageView deletePhoto;
    @FXML
    private ImageView photoRight;
    @FXML
    private ImageView photoLeft;
    @FXML
    private ComboBox colore;
    @FXML
    private Button addImageButton;
    @FXML
    private Button saveImageButton;
    @FXML
    private TextField colorPrice;

    CatalogoModel catalogo = CatalogoModel.getInstance();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sono già presenti delle immagini, carico quelle già esistenti
        ArrayList <Optional> oldPhotos = catalogo.getSelectedAuto().getOptionalByCategory(TipoOptional.colore);
        if(oldPhotos != null){

        }

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
                () -> currentImages.size() > 1 && currentIndex.get() < currentImages.size() - 1,
                currentIndex, currentImages
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
        addImageButton.disableProperty().bind(colore.valueProperty().isNull());
        saveImageButton.disableProperty().bind(Bindings.size(currentImages).isEqualTo(0));
    }

    public void getNextPhoto(){
        if (currentIndex.get() < currentImages.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            addedImages.setImage(currentImages.get(currentIndex.get()));
        }
    }
    public void getPreviousPhoto(){
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
            addedImages.setImage(currentImages.get(currentIndex.get()));
        }
    }

    private void deletePhoto(){
        currentImages.remove(currentIndex.get());
        if (currentImages.isEmpty()) {
            currentIndex.set(-1);
            addedImages.setImage(new Image(getClass().getResourceAsStream("/img/icons/addImage.png")));
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
        System.out.println(allImages);
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
            addedImages.setImage(image);
        }
    }
}
