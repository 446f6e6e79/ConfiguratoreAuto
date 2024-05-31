package org.example.configuratoreauto.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.RegistroModel;

import java.io.File;
import java.util.regex.Pattern;

import static org.example.configuratoreauto.Macchine.AutoUsata.isValidTarga;

public class AddAutoUsataController {
    RegistroModel registro = RegistroModel.getInstance();
    /** ArrayList osservabile, contenente le immagini inserite */
    private final ObservableList<Image> usedImages = FXCollections.observableArrayList();


    private BooleanBinding formValid;
    @FXML
    private ComboBox<Marca> marcaComboBox;
    @FXML
    private TextField modelloTextField;
    @FXML
    private TextField targaTextField;
    @FXML
    private TextField kmTextField;
    @FXML
    private Label saveLabel;
    @FXML
    private HBox imageGrid;

    @FXML
    private void initialize() {
        marcaComboBox.getItems().setAll(Marca.values());

        //Impongo lettere upperCase nel input della targa
        targaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            //Se è stato inserito un carattere, lo trasforma in upperCase
            if (newValue != null){
                targaTextField.setText(newValue.toUpperCase());
            }
        });

        //Impongo numeri interi
        kmTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> InputController.checkValidInt(event, kmTextField));

        formValid = Bindings.createBooleanBinding(() ->
                        isValidTarga(targaTextField.getText()) &&
                                !modelloTextField.getText().isEmpty() &&
                                !targaTextField.getText().isEmpty() &&
                                !kmTextField.getText().isEmpty() &&
                                usedImages.size() == 4,
                targaTextField.textProperty(),
                modelloTextField.textProperty(),
                kmTextField.textProperty(),
                usedImages
        );
    }

    @FXML
    private void imageFileInput(MouseEvent event) {
        ImageView clickedImageView = (ImageView) event.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png"),
                new FileChooser.ExtensionFilter("Image files", "*.jpg"),
                new FileChooser.ExtensionFilter("Image files", "*.jpeg")
        );

        File imageFile = fileChooser.showOpenDialog(new Stage());

        //Gestione dell'immagine appena aggiunta
        if (imageFile != null) {
            try {
                String imageUrl = imageFile.toURI().toString();
                Image image = new Image(imageUrl);

                // Verifica che l'immagine non sia già dentro l'array
                boolean imageAlreadyUsed = false;
                for (Image usedImage : usedImages) {
                    if (usedImage.getUrl().equals(image.getUrl())) {
                        imageAlreadyUsed = true;
                        break;
                    }
                }
                //Se non ho provato ad inserire la stessa immagine
                if (!imageAlreadyUsed) {
                    saveLabel.setText("Immagine inserita correttamente");
                    // If the image is not already used, proceed with adding or updating
                    int index = usedImages.indexOf(image);
                    // Se l'immagine è nuova
                    if (index == -1) {
                        usedImages.add(image);
                    }
                    // Se c'è un'immagine viene sovrascritta
                    else {
                        usedImages.set(index, image);
                    }
                    clickedImageView.setImage(image);
                } else {
                    saveLabel.setText("Questa immagine è già stata inserita");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    /**
     *  Salva la macchina usata appena inserita all'interno del preventivo.
     *  Prima di fare ciò,
     */
    @FXML
    private void salvaButton() {
        if (formValid.get()) {
            // Se il form è valido, esegui le operazioni di salvataggio
            Marca marca = marcaComboBox.getValue();
            String modello = modelloTextField.getText();
            String targa = targaTextField.getText();
            int km = Integer.parseInt(kmTextField.getText());

            /*
            *   Aggiungo all'auto usata le immagini inserite
            * */
            AutoUsata autoUsata = new AutoUsata(marca, modello, targa, km);
            for(Image img: usedImages){
                System.out.println(img.getUrl().toString().substring(5));
                autoUsata.addImage(new Immagine("", null, img.getUrl().toString().substring(5)));
            }
            autoUsata.addToLocalImages();

            saveLabel.setText("Auto usata salvata con successo!");
            registro.currentPreventivo.setUsata(autoUsata);
            registro.addData(registro.currentPreventivo);
            ((Stage) saveLabel.getScene().getWindow()).close();
        }
        else{
            // Se il form non è valido, visualizza un messaggio di errore e evidenzia i campi non validi
            highlightFields();
        }
    }

    /**
     * Segnala i campi errati, con un messaggio di errore, in modo
     * da aiutare l'utente nell'inserimento
     */
    private void highlightFields() {
        marcaComboBox.setStyle("-fx-border-color: black;");
        modelloTextField.setStyle("-fx-border-color: black;");
        targaTextField.setStyle("-fx-border-color: black;");
        kmTextField.setStyle("-fx-border-color: black;");
        imageGrid.setStyle("");

        String errorMessage = "Completa tutti i campi!";

        if(usedImages.size() != 4){
            imageGrid.setStyle("-fx-border-color: red; -fx-border-width: 3");
            errorMessage =  "Inserisci le immagini richieste!";
        }
        if (!isValidTarga(targaTextField.getText())) {
            targaTextField.setStyle("-fx-border-color: red;");
            errorMessage = "Targa errata o già presente nel registro";
        }
        if (marcaComboBox.getValue() == null) {
            marcaComboBox.setStyle("-fx-border-color: red;");
            errorMessage = "Inserisci tutti i campi!";
        }
        if (modelloTextField.getText().isEmpty()) {
            modelloTextField.setStyle("-fx-border-color: red;");
            errorMessage = "Inserisci tutti i campi!";
        }
        if (targaTextField.getText().isEmpty()) {
            targaTextField.setStyle("-fx-border-color: red;");
            errorMessage = "Inserisci tutti i campi!";
        }
        if (kmTextField.getText().isEmpty()) {
            kmTextField.setStyle("-fx-border-color: red;");
            errorMessage = "Inserisci tutti i campi!";
        }
        saveLabel.setText(errorMessage);
    }

    /**
     * Non viene aggiunta alcuna auto usata. Viene impostato lo stato del preventivo e
     *  vengono aggiornate le date di scadenza/consegna
     */
    @FXML
    private void saltaButton() {
        registro.currentPreventivo.setUsata(null);
        registro.addData(registro.currentPreventivo);
        ((Stage) saveLabel.getScene().getWindow()).close();
    }
}
