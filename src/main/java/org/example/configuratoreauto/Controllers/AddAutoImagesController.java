package org.example.configuratoreauto.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.example.configuratoreauto.Controllers.InputValidation.checkValidDouble;

public class AddAutoImagesController implements Initializable {
    CatalogoModel catalogo = CatalogoModel.getInstance();
    AutoNuova tempAuto = catalogo.getTempAuto();

    /*
     *   Definisco delle variabili  Observable che permettono di implementare il pattern observer:
     *       -currentIndex è un numero intero, indica la foto attualmente selezionata
     *       -currentImages è una ArrayList, contenente le immagini
     * */
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(-1);
    private final ObservableList<Immagine> imagesCurrentColor = FXCollections.observableArrayList();

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Recupero i colori già DISPONIBILI
        ArrayList<String> availableColors = tempAuto.getUsedColors();

        //Aggiungo i colori disponibili e ne seleziono 1
        if (!availableColors.isEmpty()) {
            coloreInput.getItems().addAll(availableColors);
            coloreInput.getSelectionModel().select(0);
            currentIndex.set(0);
            //Aggiorno le immagini per il Colore selezionato
            updateDataForSelectedColor();
        }

        //Quando cambia il valore di color, viene aggiornata la lista di immagini per il Colore disponibile
        coloreInput.valueProperty().addListener((observable, oldValue, newValue) -> updateDataForSelectedColor());

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

        //Disabilito il bottone per l'aggiunta di foto se non ho inserito il Colore
        addImageButton.disableProperty().bind(coloreInput.valueProperty().isNull());

        //Disabilito il bottone per aggiungere il Colore se non sono presenti foto
        saveImageButton.disableProperty().bind(Bindings
                .size(imagesCurrentColor).isEqualTo(0)
        );

        colorPrice.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, colorPrice));
    }

    //Aggiorna la ImageView alla foto sucessiva, aggiornando l'index
    public void getNextPhoto() {
        if (currentIndex.get() < imagesCurrentColor.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            addedImagesView.setImage(imagesCurrentColor.get(currentIndex.get()).getImage());
        }
    }

    //Aggiorna la ImageView alla foto precedente, aggiornando l'index
    public void getPreviousPhoto() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
            addedImagesView.setImage(imagesCurrentColor.get(currentIndex.get()).getImage());
        }
    }

    /**
     *  Rimuove un immagine dalle liste:
     *      - lista di immagini del Colore attuale
     *      - lista di immagini completa, dell'auto temporanea
     */
    private void deletePhoto() {
        //Recuper l'oggetto immagine e lo elimino da tempAuto
        tempAuto.removeImage(imagesCurrentColor.get(currentIndex.get()));
        imagesCurrentColor.remove(currentIndex.get());

        if (imagesCurrentColor.isEmpty()) {
            currentIndex.set(-1);
            addedImagesView.setImage(new Image(getClass().getResourceAsStream("/img/icons/addImage.png")));
            tempAuto.getOptionalDisponibili().removeIf(optional -> optional.getDescrizione().equals(coloreInput.getValue()));
            coloreInput.setValue(null);
        }
        else {
            if (currentIndex.get() >= imagesCurrentColor.size()) {
                currentIndex.set(imagesCurrentColor.size() - 1);
            }
            addedImagesView.setImage(imagesCurrentColor.get(currentIndex.get()).getImage());
        }
    }

    /**
     * Aggiorna le nuove immagini ed il prezzo
     * dopo che è stato selezionato un nuovo Colore
     */
    private void updateDataForSelectedColor() {
        //Pulisco la lista delle immagini per il coloreAttuale
        imagesCurrentColor.clear();
        currentIndex.set(-1);
        //Pulisco il campo del prezzo
        colorPrice.setText("");

        //Carico le immagini già presenti per il Colore scelto
        String selectedColor = coloreInput.getValue();
        if (selectedColor != null ) {
            imagesCurrentColor.addAll(tempAuto.getImagesByColor(selectedColor));

            //Se erano già presenti delle immagini aggiorno di conseguenza tutti i campi
            if (!imagesCurrentColor.isEmpty()) {
                currentIndex.set(0);
                addedImagesView.setImage(imagesCurrentColor.get(0).getImage());

                //Aggiorno il prezzo
                for (Optional colore : tempAuto.getOptionalByCategory(TipoOptional.Colore)) {
                    if (colore.getDescrizione().equals(coloreInput.getValue())) {
                        colorPrice.setText(String.valueOf(colore.getCosto()));
                    }
                }
            }
        }
        //Non erano presenti delle immagini, setto l'immagine di default
        else {
            addedImagesView.setImage(new Image(getClass().getResourceAsStream("/img/icons/addImage.png")));
        }
    }

    /**
     * Aggiunge agli optional il Colore inserito
     * Tale modifiche saranno poi passate al vero oggetto auto solamente quando confermate
     */
    @FXML
    private void saveImages() {
        String selectedColor = coloreInput.getValue();

        //Aggiungo l'optional colore
        if (selectedColor != null) {
            tempAuto.addOptional(
                    new Optional(
                            TipoOptional.Colore,
                            selectedColor,
                            Double.parseDouble(colorPrice.getText())
                    )
            );
            //Se ho aggiunto un nuovo colore, lo aggiungo alla comboBox
            if(!coloreInput.getItems().contains(selectedColor)){
                coloreInput.getItems().add(selectedColor);
            }
        }

        coloreInput.setValue(null);
        colorPrice.clear();
        imagesCurrentColor.clear();
        currentIndex.set(-1);
    }


    @FXML
    private void imageFileInput() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png"),
                new FileChooser.ExtensionFilter("Image files", "*.jpg")
        );

        File imageFile = fileChooser.showOpenDialog(new Stage());

        //Gestione dell'immagine appena aggiunta
        if (imageFile != null) {
            Immagine img = new Immagine(coloreInput.getValue(), imageFile.toURI().toString().substring(5));

            //Aggiunge l'immagine all'auto temporanea
            tempAuto.addImage(img);

            //Aggiungo l'immagine alla lista delle immagini del Colore corrente
            imagesCurrentColor.add(img);

            //Imposto il current index alla nuova immagine aggiunta
            currentIndex.set(imagesCurrentColor.size() - 1);

            //Aggiungo l'immagine appena inserita alla ImageView
            addedImagesView.setImage(img.getImage());
        }
    }

    //Carica la pagina successiva
    @FXML
    private void nextPage(){
        if(!tempAuto.getUsedColors().isEmpty()) {
            TabPane tabPane = (TabPane) colorPrice.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addOptionals.fxml");
        }
        else{
            PageLoader.showErrorPopup("Errore", "Devi aggiungere almeno un colore per procedere.");
        }
    }

    //Carica la pagina precedente
    public void goBack(){
        TabPane tabPane = (TabPane) colorPrice.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
        PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addModel.fxml");
    }
}
