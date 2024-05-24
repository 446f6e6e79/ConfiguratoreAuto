package org.example.configuratoreauto.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.RegistroModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddAutoUsataController {

    RegistroModel registro = RegistroModel.getInstance();
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
    private Button salvaButton;

    private List<Immagine> immagini = new ArrayList<>();

    @FXML
    private void initialize() {
        marcaComboBox.getItems().setAll(Marca.values());


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

        salvaButton.disableProperty().bind(formValid.not());
    }

    @FXML
    private void imageFileInput(MouseEvent event) {
        ImageView clickedImageView = (ImageView) event.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png"),
                new FileChooser.ExtensionFilter("Image files", "*.jpg")
        );

        File imageFile = fileChooser.showOpenDialog(new Stage());

        //Gestione dell'immagine appena aggiunta
        if (imageFile != null) {
            try {
                String imageUrl = imageFile.toURI().toString();
                Image image = new Image(imageUrl);
                clickedImageView.setImage(image);
                //Aggiungo l'immagine alla lista immagini aggiunte
                if (!usedImages.contains(image)) {
                    usedImages.add(image);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    private void salvaButton() {
        if (formValid.get()) {
            // Se il form è valido, esegui le operazioni di salvataggio
            Marca marca = marcaComboBox.getValue();
            String modello = modelloTextField.getText();
            String targa = targaTextField.getText();
            int km = Integer.parseInt(kmTextField.getText());

            AutoUsata autoUsata = new AutoUsata(marca, modello, targa, km);

            autoUsata.addToLocalImages();

            saveLabel.setText("Auto usata salvata con successo!");
            registro.currentPreventivo.setUsata(autoUsata);
            registro.addData(registro.currentPreventivo);
            ((Stage) saveLabel.getScene().getWindow()).close();
        } else {
            // Se il form non è valido, visualizza un messaggio di errore e evidenzia i campi non validi
            ;
            highlightInvalidFields();
        }
    }

    private void highlightInvalidFields() {
        marcaComboBox.setStyle("-fx-border-color: black;");
        modelloTextField.setStyle("-fx-border-color: black;");
        targaTextField.setStyle("-fx-border-color: black;");
        kmTextField.setStyle("-fx-border-color: black;");

        if (marcaComboBox.getValue() == null) {
            marcaComboBox.setStyle("-fx-border-color: red;");
        }
        if (!isValidTarga(targaTextField.getText())) {
            targaTextField.setStyle("-fx-border-color: red;");
        }
        if (modelloTextField.getText().isEmpty()) {
            modelloTextField.setStyle("-fx-border-color: red;");
        }
        if (targaTextField.getText().isEmpty()) {
            targaTextField.setStyle("-fx-border-color: red;");
        }
        if (kmTextField.getText().isEmpty()) {
            kmTextField.setStyle("-fx-border-color: red;");
        }
    }

    @FXML
    private void saltaButton() {
        registro.currentPreventivo.setUsata(null);
        registro.addData(registro.currentPreventivo);
        ((Stage) saveLabel.getScene().getWindow()).close();
    }

    private boolean isValidTarga(String targa) {
        String regex = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$";
        return Pattern.matches(regex, targa);
    }


}
