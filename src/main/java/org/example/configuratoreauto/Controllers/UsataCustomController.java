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

public class UsataCustomController {

    RegistroModel registro = RegistroModel.getInstance();
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
    private Label saveLabel;
    @FXML
    private Button salvaButton;

    private List<Immagine> immagini = new ArrayList<>();

    @FXML
    private void initialize() {
        marcaComboBox.getItems().setAll(Marca.values());

        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
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
        System.out.println("CLICCATOOO");
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
        if (marcaComboBox.getValue() != null &&
                isValidTarga(targaTextField.getText()) &&
                !modelloTextField.getText().isEmpty() &&
                !targaTextField.getText().isEmpty() &&
                !kmTextField.getText().isEmpty()) {

            Marca marca = marcaComboBox.getValue();
            String modello = modelloTextField.getText();
            String targa = targaTextField.getText();
            int km = Integer.parseInt(kmTextField.getText());

            AutoUsata autoUsata = new AutoUsata(marca, modello, targa, km);

            for (Image img : usedImages) {
                autoUsata.addImage(new Immagine(null, autoUsata, img.getUrl().substring(5)));
            }

            saveLabel.setText("Auto usata salvata con successo!");
            registro.currentPreventivo.setUsata(autoUsata);
            registro.addData(registro.currentPreventivo);
            ((Stage) saveLabel.getScene().getWindow()).close();
        } else {
            saveLabel.setText("Compila tutti i campi e aggiungi almeno 4 immagini.");
        }
    }

    @FXML
    private void saltaButton() {
        registro.currentPreventivo.setUsata(null);
        registro.addData(registro.currentPreventivo);
        ((Stage) saveLabel.getScene().getWindow()).close();
    }

    private boolean isValidTarga(String targa) {
        String regex = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$"; // Adjust this regex based on actual targa format
        return Pattern.matches(regex, targa);
    }


}
