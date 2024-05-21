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
import org.example.configuratoreauto.Macchine.*;

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
    private ObservableList<Image> imagesCurrentColor = FXCollections.observableArrayList();
    private ObservableList<Immagine> allImages = FXCollections.observableArrayList();

    @FXML
    private ImageView addedImagesView;
    @FXML
    private ImageView deletePhoto;
    @FXML
    private ImageView photoRight;
    @FXML
    private ImageView photoLeft;
    @FXML
    private ComboBox<String> coloreInput;
    @FXML
    private Button addImageButton;
    @FXML
    private Button saveImageButton;
    @FXML
    private TextField colorPrice;
    @FXML
    private Button avantiButton;

    CatalogoModel catalogo = CatalogoModel.getInstance();
    AutoNuova currentAuto = catalogo.getSelectedAuto();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Recupero i colori DISPONIBILI
        ArrayList <String> availableColors = currentAuto.getUsedColors();

        /*  Se erano già presenti delle immagini, le carico
        * */
        if(!availableColors.isEmpty()){
            //Aggiungo tutte le immagini già presenti per tale modello
            allImages.addAll(currentAuto.getImmagini());
            //Aggiungo i colori disponibili e ne seleziono 1
            coloreInput.getItems().addAll(availableColors);
            coloreInput.getSelectionModel().select(0);
            currentIndex.set(0);
            //Aggiorno le immagini per il colore selezionato
            updateDataForSelectedColor();
        }

        //Quando cambia il valore di color, viene
        coloreInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateDataForSelectedColor();
        });

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
                () -> imagesCurrentColor.size() > 1 && currentIndex.get() < imagesCurrentColor.size() - 1,
                currentIndex, imagesCurrentColor
        ));

        /*
         *   Setto visibile il tasto per eliminare la foto solamente nel momento in cui è
         *   presente almeno un immagine
         * */
        deletePhoto.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> !imagesCurrentColor.isEmpty(),
                imagesCurrentColor
        ));
        /*
         *   Setto le action per i 3 bottoni presenti nell'inserimento dell'immagine
         * */
        photoLeft.setOnMouseClicked(e -> getPreviousPhoto());
        photoRight.setOnMouseClicked(e -> getNextPhoto());
        deletePhoto.setOnMouseClicked(e -> deletePhoto());

        //Disabilito il bottone per l'aggiunta di foto se non ho inserito il colore
        addImageButton.disableProperty().bind(coloreInput.valueProperty().isNull());

        //Disabilito il bottone per aggiunger il colore se non sono presenti foto
        saveImageButton.disableProperty().bind(Bindings
                .size(imagesCurrentColor).isEqualTo(0)
        );

        //Disabilito il bottone avanti fino a che non è stata inserita una nuova immagine
        avantiButton.disableProperty().bind(Bindings
                .size(allImages).isEqualTo(0)
        );
    }

    public void getNextPhoto(){
        if (currentIndex.get() < imagesCurrentColor.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            addedImagesView.setImage(imagesCurrentColor.get(currentIndex.get()));
        }
    }
    public void getPreviousPhoto(){
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
            addedImagesView.setImage(imagesCurrentColor.get(currentIndex.get()));
        }
    }

    private void deletePhoto(){
        imagesCurrentColor.remove(currentIndex.get());
        if (imagesCurrentColor.isEmpty()) {
            currentIndex.set(-1);
            addedImagesView.setImage(new Image(getClass().getResourceAsStream("/img/icons/addImage.png")));
        } else {
            if (currentIndex.get() >= imagesCurrentColor.size()) {
                currentIndex.set(imagesCurrentColor.size() - 1);
            }
            addedImagesView.setImage(imagesCurrentColor.get(currentIndex.get()));
        }
    }

    /**
     * Aggiorna le nuove immagini ed il prezzo
     *  dopo che è stato selezionato un nuovo colore
     */
    private void updateDataForSelectedColor() {
        System.out.println("COLOREEE:"+ coloreInput.getValue());
        System.out.println("Size:" +allImages.size());
        imagesCurrentColor.clear();
        currentIndex.set(-1);

        String selectedColor = coloreInput.getValue();
        if (selectedColor != null) {
            for (Immagine img : allImages) {
                if (img.getColor().equals(selectedColor)) {
                    imagesCurrentColor.add(img.getImage());
                }
            }

            if (!imagesCurrentColor.isEmpty()) {
                currentIndex.set(0);
                addedImagesView.setImage(imagesCurrentColor.get(0));

                //Aggiorno di conseguenza il prezzo
                for(Optional colore: currentAuto.getOptionalByCategory(TipoOptional.colore)){
                    if(colore.getDescrizione().equals(coloreInput.getValue())){
                        colorPrice.setText(String.valueOf(colore.getCosto()));
                    }
                }

            } else {
                addedImagesView.setImage(new Image(getClass().getResourceAsStream("/img/icons/addImage.png")));
            }
        }
    }


    /*
     *   Salvo localmente le immagini per tale colore. Verranno caricate
     * */
    @FXML
    private void saveImages(){
        if(isValidCosto()){
            for(Image img: imagesCurrentColor){
                allImages.add(new Immagine(coloreInput.getValue(), currentAuto, img));
            }

            //Aggiorno gli optional disponibili per auto
            ArrayList <String> usedColors = currentAuto.getUsedColors();
            if(usedColors.contains(coloreInput.getValue())){
                currentAuto.addOptional(
                        new Optional(
                              TipoOptional.colore,
                                coloreInput.getValue(),
                                Double.parseDouble(colorPrice.getText())
                        )
                );
            }
            coloreInput.getItems().add(coloreInput.getValue());
            imagesCurrentColor.clear();
            currentIndex.set(-1);
        }
    }

    public boolean isValidCosto(){
        return true;
    }

    @FXML
    private void imageFileInput(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png"),
                new FileChooser.ExtensionFilter("Image files", "*.jpg")
        );

        File imageFile = fileChooser.showOpenDialog(new Stage());

        //Gestione dell'immagine appena aggiunta
        if(imageFile != null){
            Image image = new Image(imageFile.toURI().toString());
            //Aggiungo l'immagine alla lista delle immagini del colore corrente
            imagesCurrentColor.add(image);
            //Imposto il current index alla nuova immagine aggiunta
            currentIndex.set(imagesCurrentColor.size()-1);
            //Aggiungo l'immagine appena inserita alla ImageView
            addedImagesView.setImage(image);
        }
    }
}