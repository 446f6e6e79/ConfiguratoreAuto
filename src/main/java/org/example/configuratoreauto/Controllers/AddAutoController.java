package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Mesi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAutoController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField modello;
    @FXML
    private ComboBox<Marca> brand;
    @FXML
    private TextField descrizione;
    @FXML
    private TextField lunghezza;
    @FXML
    private TextField altezza;
    @FXML
    private TextField larghezza;
    @FXML
    private TextField peso;
    @FXML
    private TextField volume;
    @FXML
    private TextField costoBase;
    @FXML
    private GridPane selectedMesi;
    @FXML
    private TextField scontoInput;
    @FXML
    private Button avantiButton;
    @FXML
    private Button addScontoButton;
    @FXML
    private Text message;

    CatalogoModel catalogo = CatalogoModel.getInstance();
    double sconti[] = new double[12];
    private static final String[] MONTH_NAMES = {
            "Gennaio", "Febbraio", "Marzo", "Aprile",
            "Maggio", "Giugno", "Luglio", "Agosto",
            "Settembre", "Ottobre", "Novembre", "Dicembre"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainBorderPane.prefWidthProperty().bind(mainPane.widthProperty());
        mainBorderPane.prefHeightProperty().bind(mainPane.heightProperty());

        /*
        *   Valorizzo i campi per le varie choiceBox
        * */
        brand.getItems().addAll(Marca.values());

        avantiButton.setDisable(true);
        /*
        *   Collego la proprietà disable del bottone AVANTI:
        *    - almeno un campo non è valorizzato -> Disable = TRUE
        *    - tutti i campi sono valorizzati -> Disable = FALSE
        * */
        avantiButton.disableProperty().bind(
                modello.textProperty().isEmpty()
                .or(descrizione.textProperty().isEmpty())
                .or(brand.valueProperty().isNull())
                .or(lunghezza.textProperty().isEmpty())
                .or(altezza.textProperty().isEmpty())
                .or(larghezza.textProperty().isEmpty())
                .or(peso.textProperty().isEmpty())
                .or(volume.textProperty().isEmpty())
                .or(costoBase.textProperty().isEmpty())
        );
        addScontoButton.disableProperty().bind(
                scontoInput.textProperty().isEmpty()
        );

        /*
        *   In caso il segretario stia modificando un auto, carico i dati già presenti
        * */
        if(catalogo.getSelectedAuto() != null){
            AutoNuova aN = catalogo.getSelectedAuto();
            sconti = aN.getScontoPerMese();
            modello.setText(aN.getModello());
            brand.setValue(aN.getMarca());
            descrizione.setText(aN.getDescrizione());
            lunghezza.setText(String.valueOf(aN.getDimensione().getLunghezza()));
            altezza.setText(String.valueOf(aN.getDimensione().getAltezza()));
            larghezza.setText(String.valueOf(aN.getDimensione().getLarghezza()));
            peso.setText(String.valueOf(aN.getDimensione().getPeso()));
            volume.setText(String.valueOf(aN.getDimensione().getVolumeBagagliaglio()));
            costoBase.setText(String.valueOf(aN.getCostoBase()));
            addSconto();
        }
    }


//    public void backClicked(){
//        try {
//            TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
//            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
//            AnchorPane catalogoNode;
//
//            catalogoNode = loader.load();
//            tab.setContent(catalogoNode); // Imposta il nuovo contenuto del tab "Catalogo"
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void addSconto(){
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                int i = r * 4 + c;
                ToggleButton toggleButton = (ToggleButton) selectedMesi.getChildren().get(i);
                if (toggleButton.isSelected()) {
                    sconti[i] = Double.parseDouble(scontoInput.getText());
                    toggleButton.setSelected(false);
                }
                if(sconti[i] != 0) {
                    toggleButton.setText(Mesi.values()[i].toString() + "\n" + sconti[i] + "%");
                }
            }
        }
    }
    @FXML
    private void addModello() {
        if (isValidModello(modello) && isValidDouble(lunghezza) && isValidDouble(altezza) && isValidDouble(larghezza)
                && isValidDouble(peso) && isValidDouble(volume) && isValidDouble(costoBase)) {
            if(catalogo.getSelectedAuto() == null){
                catalogo.setSelectedAuto(new AutoNuova(
                        catalogo.getUniqueId(),
                        brand.getValue(),
                        modello.getText(),
                        new Dimensione(
                                Double.parseDouble(lunghezza.getText()),
                                Double.parseDouble(altezza.getText()),
                                Double.parseDouble(larghezza.getText()),
                                Double.parseDouble(peso.getText()),
                                Double.parseDouble(volume.getText())
                        ),
                        descrizione.getText(),
                        Double.parseDouble(costoBase.getText()),
                        sconti
                ));
            }
            else{
                //Dovrei fare set di ogni nuovo dato. Forse è meglio creare un costruttore con tutto a null, valorizzare dopo
            }
            loadAddImagesPage();
        }
        else{
            message.setText("Errore nell'inserimento di alcuni campi");
        }
    }

    private boolean isValidDouble(TextField tf){
        try {
            double value = Double.parseDouble(tf.getText());
            if(value < 0){
                tf.clear();
                tf.requestFocus();
            }
        } catch (NumberFormatException e) {
            tf.clear();
            tf.requestFocus();
            message.setText("Errore nell'inserimento di alcuni campi");
            return false;
        }
        return true;
    }

    private boolean isValidModello(TextField tf) {
        if(tf.getText().matches("[a-zA-Z0-9 ]+")){
            return true;
        }
        tf.clear();
        tf.requestFocus();
        return false;
    }

    private void loadAddImagesPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/segretarioView/addImages.fxml"));
            Parent addImagesView = loader.load();

            // Assuming you are replacing the scene of the current stage
            Stage stage = (Stage) avantiButton.getScene().getWindow();
            stage.setScene(new Scene(addImagesView));

            // Alternatively, if you want to replace the content of a specific pane:
            // ((BorderPane) avantiButton.getScene().getRoot()).setCenter(addImagesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
