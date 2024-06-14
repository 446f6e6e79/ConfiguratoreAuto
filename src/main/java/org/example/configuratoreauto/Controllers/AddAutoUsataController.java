package org.example.configuratoreauto.Controllers;

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

import static org.example.configuratoreauto.Macchine.AutoUsata.isValidTarga;

public class AddAutoUsataController {
    RegistroModel registroModel = RegistroModel.getInstance();
    /** ArrayList osservabile, contenente le immagini inserite */
    private final ObservableList<Image> usedImages = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Marca> marcaComboBox;
    @FXML
    private TextField modelloTextField;
    @FXML
    private TextField targaTextField;
    @FXML
    private TextField kmTextField;
    @FXML
    private HBox imageGrid;
    @FXML
    private Button salvaButton;

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
        kmTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> InputValidation.checkValidInt(event, kmTextField));

        //Blocco il bottone salva fino a che non sono stati compilati tutti i campi
        salvaButton.disableProperty().bind(
            marcaComboBox.getSelectionModel().selectedItemProperty().isNull()
            .or(modelloTextField.textProperty().isEmpty())
            .or(targaTextField.textProperty().isEmpty())
            .or(kmTextField.textProperty().isEmpty())
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
                    int index = usedImages.indexOf(clickedImageView.getImage());
                    // Se l'immagine è nuova
                    if (index == -1) {
                        usedImages.add(image);
                    }
                    // Se c'è un'immagine viene sovrascritta
                    else {
                        usedImages.set(index, image);
                    }
                    clickedImageView.setImage(image);
                }
                //Ho inserito un immagine già presente
                else {
                    PageLoader.showErrorPopup("Errore immagine", "Quest'immagine è già stata inserita");
                }
            }
            catch(Exception e) {
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
        //Controllo sui valori inseriti
        if(isValidImageInput() &&
            isValidTarga(targaTextField.getText()) &&
            InputValidation.isValidDoubleTextField(true, kmTextField)
        )
        {
            try {
                // Se il form è valido, esegui le operazioni di salvataggio
                Marca marca = marcaComboBox.getValue();
                String modello = modelloTextField.getText();
                String targa = targaTextField.getText();
                int km = Integer.parseInt(kmTextField.getText());

                /*
                 *   Aggiungo all'auto usata le immagini inserite
                 * */
                AutoUsata autoUsata = new AutoUsata(marca, modello, targa, km);
                for (Image img : usedImages) {
                    autoUsata.addImage(new Immagine("", img.getUrl().substring(5)));
                }
                autoUsata.addToLocalImages();
                saveCurrentPreventivo(autoUsata);
            }
            catch(Exception e) {
                PageLoader.showErrorPopup("Errore", e.getMessage());
            }
        }
    }

    /**
     * Segnala i campi errati, con un messaggio di errore, in modo
     * da aiutare l'utente nell'inserimento
     */
    private boolean isValidImageInput() {
        imageGrid.setStyle("");
        if(usedImages.size() != 4){
            imageGrid.setStyle("-fx-border-color: red; -fx-border-width: 3");
            return false;
        }
        return true;
    }


    /**
     * Non viene aggiunta alcuna auto usata. Viene impostato lo stato del preventivo e
     *  vengono aggiornate le date di scadenza/consegna
     */
    @FXML
    private void saltaButton() {
        saveCurrentPreventivo(null);
    }

    /**
     * Metodo che si occupa di salvare nel registro il preventivo attuale con apportate modifiche
     */
    private void saveCurrentPreventivo(AutoUsata autoUsata){
        registroModel.getCurrentPreventivo().setUsata(autoUsata);
        registroModel.addData(registroModel.getCurrentPreventivo());
        ((Stage) kmTextField.getScene().getWindow()).close();
    }
}
