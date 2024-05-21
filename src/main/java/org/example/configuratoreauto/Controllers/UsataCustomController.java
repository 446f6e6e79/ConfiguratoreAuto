package org.example.configuratoreauto.Controllers;

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
import javafx.beans.binding.Bindings;


public class UsataCustomController {

    RegistroModel registro = RegistroModel.getInstance();
    private ObservableList<Image> usedImages;

    @FXML
    private ComboBox<Marca> marcaComboBox;
    @FXML
    private TextField modelloTextField;
    @FXML
    private TextField targaTextField;
    @FXML
    private TextField kmTextField;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField colorTextField;
    @FXML
    private Label saveLabel;
    @FXML
    private Button salvaButton;


    private List<Immagine> immagini = new ArrayList<>();
    private int currentImageIndex = 0;


    @FXML
    private void initialize() {
        marcaComboBox.getItems().setAll(Marca.values());
        salvaButton.disabledProperty().bind(
                Bindings.size(usedImages.getImageUrls()).lessThan(4)
        );
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
                usedImages.add(image);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void salvaButton() {
        if (marcaComboBox.getValue() != null &&
                !modelloTextField.getText().isEmpty() &&
                !targaTextField.getText().isEmpty() &&
                !kmTextField.getText().isEmpty() &&
                immagini.size() >= 4) {

            Marca marca = marcaComboBox.getValue();
            String modello = modelloTextField.getText();
            String targa = targaTextField.getText();
            int km = Integer.parseInt(kmTextField.getText());

            AutoUsata autoUsata = new AutoUsata(marca, modello, targa, km);

            for (Immagine immagine : immagini) {

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
}
