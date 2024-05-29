package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Mesi;
import org.example.configuratoreauto.Utenti.UserModel;

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
    private TextArea descrizione;
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
    @FXML
    private Button eliminaButton;

    CatalogoModel catalogo = CatalogoModel.getInstance();
    double[] sconti = new double[12];
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

        //Blocco il tasto aggiungi sconto fino a che non è stato inserito un valore per sconto
        addScontoButton.disableProperty().bind(
                scontoInput.textProperty().isEmpty()
        );

        /*
        *   Blocco input NON validi sui campi numerici:
        * */
        lunghezza.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            System.out.println("Altezza:"+mainPane.getHeight()+" Larghezza: "+mainPane.getWidth());
            checkValidDouble(event, lunghezza);
        });
        larghezza.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            checkValidDouble(event, larghezza);
        });
        altezza.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            checkValidDouble(event, altezza);
        });
        peso.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            checkValidDouble(event, peso);
        });
        volume.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            checkValidDouble(event, volume);
        });
        scontoInput.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            checkValidDouble(event, scontoInput);
        });

        AutoNuova tempAuto = catalogo.getTempAuto();
        eliminaButton.setVisible(false);
        //Ottengo la copia dell'auto, solo se non già presente
        if(tempAuto == null){
            catalogo.generateTempAuto(catalogo.getSelectedAuto());
            tempAuto = catalogo.getTempAuto();
        }
        /*
        *   In caso il segretario stia modificando un auto, carico i dati già presenti
        * */
        if(catalogo.getSelectedAuto() != null || tempAuto.getModello() != null){
            eliminaButton.setVisible(true);
            sconti = tempAuto.getScontoPerMese();
            setScontiOutput();
            modello.setText(tempAuto.getModello());
            brand.setValue(tempAuto.getMarca());
            descrizione.setText(tempAuto.getDescrizione());
            lunghezza.setText(String.valueOf(tempAuto.getDimensione().getLunghezza()));
            altezza.setText(String.valueOf(tempAuto.getDimensione().getAltezza()));
            larghezza.setText(String.valueOf(tempAuto.getDimensione().getLarghezza()));
            peso.setText(String.valueOf(tempAuto.getDimensione().getPeso()));
            volume.setText(String.valueOf(tempAuto.getDimensione().getVolumeBagagliaglio()));
            costoBase.setText(String.valueOf(tempAuto.getCostoBase()));
            addSconto();
        }
    }
    @FXML
    public void goBack(){
        //Resetto tempAuto a null, cancellando tutte le modifiche effettuate
        catalogo.setTempAuto(null);
        try {
            TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
            BorderPane catalogoNode;

            catalogoNode = loader.load();
            tab.setContent(catalogoNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void setScontiOutput(){
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                int i = r * 4 + c;
                ToggleButton toggleButton = (ToggleButton) selectedMesi.getChildren().get(i);
                if (sconti[i] != 0) {
                    toggleButton.setText(Mesi.values()[i].toString() + "\n" + sconti[i] + "%");
                }
            }
        }
    }
    @FXML
    private void addSconto() {
        try {
            int scontoValue = Integer.parseInt(scontoInput.getText());

            if (scontoValue >= 0 && scontoValue <= 100) {
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 4; c++) {
                        int i = r * 4 + c;
                        ToggleButton toggleButton = (ToggleButton) selectedMesi.getChildren().get(i);
                        if (toggleButton.isSelected()) {
                            sconti[i] = Double.parseDouble(scontoInput.getText());
                            toggleButton.setSelected(false);
                        }
                        if (sconti[i] != 0) {
                            toggleButton.setText(Mesi.values()[i].toString() + "\n" + sconti[i] + "%");
                        }
                    }
                }
                // Reset the style if the input is valid
                scontoInput.setStyle("");
            } else {
                throw new NumberFormatException("Sconto fuori intervallo");
            }
        } catch (NumberFormatException e) {
            scontoInput.clear();
            if (scontoInput != null) {
                scontoInput.setStyle("-fx-border-color: red;");
            }
            scontoInput.setAccessibleHelp("Inserire un valore tra 0 e 100");
        }
    }

    @FXML
    private void eliminaAuto(){
        // Create a confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.WARNING);
        confirmation.setTitle("Elimina");
        confirmation.setHeaderText("Sei sicuro di voler eliminare quest'auto?");


        // Customize the buttons in the dialog
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                catalogo.getAllData().remove(catalogo.getSelectedAuto());
                goBack();
            }
        });
    }
    @FXML
    private void addModello() {
        if (isValidModello(modello) && isValidDouble(lunghezza) && isValidDouble(altezza) && isValidDouble(larghezza)
                && isValidDouble(peso) && isValidDouble(volume) && isValidDouble(costoBase)) {

            //Setto i parametri inseriti all'auto temporanea
            catalogo.getTempAuto().setMarca(brand.getValue());
            catalogo.getTempAuto().setModello(modello.getText());
            catalogo.getTempAuto().setDimensione(new Dimensione(
                            Double.parseDouble(lunghezza.getText()),
                            Double.parseDouble(altezza.getText()),
                            Double.parseDouble(larghezza.getText()),
                            Double.parseDouble(peso.getText()),
                            Double.parseDouble(volume.getText())
                    ));
            catalogo.getTempAuto().setDescrizione(descrizione.getText());
            catalogo.getTempAuto().setCostoBase(Double.parseDouble(costoBase.getText()));
            catalogo.getTempAuto().setScontoPerMese(sconti);

            //Carico la pagina successiva
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

    //Carico la pagina successiva
    private void loadAddImagesPage() {
        try {
            TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/segretarioView/addImages.fxml"));
            AnchorPane imageNode;
            imageNode = loader.load();

            tab.setContent(imageNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *  Funzione che blocca input non validi, nei TextField che richiedono l'inserimento di campi double
     * @param event evento KeyEvent, rappresenta la pressione di un tasto
     * @param tf TextField, campo di input
     */
    private void checkValidDouble(KeyEvent event, TextField tf){
        //Leggo il carattere che ha generato l'evento
        String character = event.getCharacter();

        /*
            Blocco l'input di un qualsiasi tasto, diverso da:
                - numero da 0 - 9
                - punto
         */
        if (!character.matches("[0-9.]")) {
            event.consume();
        }
        //Blocca la presenza di più punti
        if (character.equals(".") && tf.getText().contains(".")) {
            event.consume();
        }
    }
}
