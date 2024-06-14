package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Mesi;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.configuratoreauto.Controllers.InputValidation.*;

/**
 * Classe che funge da controller per la pagina addModel.fxml.
 * Permette ad un segretario di aggiungere / modificare i dati di una macchina
 */
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
    private Button eliminaButton;

    CatalogoModel catalogoModel = CatalogoModel.getInstance();
    int[] sconti = new int[12];

    /**
     * Valorizza i campi all'interno della pagina, al suo caricamento
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainBorderPane.prefWidthProperty().bind(mainPane.widthProperty());
        mainBorderPane.prefHeightProperty().bind(mainPane.heightProperty());

        //Valorizzo i campi per la choiceBox della marca
        brand.getItems().addAll(Marca.values());

        /*
        *   Collego la proprietà disable del bottone AVANTI:
        *    - almeno un campo non è valorizzato -> Disable = TRUE
        *    - tutti i campi sono valorizzati -> Disable = FALSE
        * */
        avantiButton.setDisable(true);
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


        //Blocco input NON validi sui campi numerici della pagina
        lunghezza.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, lunghezza));
        larghezza.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, larghezza));
        altezza.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, altezza));
        peso.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, peso));
        volume.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, volume));
        scontoInput.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidInt(event, scontoInput));

        //Genero un auto TEMPORANEA, alla quale saranno applicate le modifiche
        AutoNuova tempAuto = catalogoModel.getTempAuto();
        eliminaButton.setVisible(false);

        //Ottengo la copia dell'auto, solo se non già presente
        if(tempAuto == null){
            catalogoModel.generateTempAuto(catalogoModel.getSelectedAuto());
            tempAuto = catalogoModel.getTempAuto();
        }

        /*
        *   In caso il segretario stia modificando un auto, carico i dati già presenti
        * */
        if(catalogoModel.getSelectedAuto() != null || tempAuto.getModello() != null){
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
            setScontiOutput();
        }
    }

    /**
     * Aggiorna i valori nella tabella degli sconti, a seconda del contenuto del vettore scontiPerMese
     */
    private void setScontiOutput(){
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                int i = r * 4 + c;
                ToggleButton toggleButton = (ToggleButton) selectedMesi.getChildren().get(i);
                if (sconti[i] != 0) {
                    toggleButton.setText(Mesi.values()[i].toString() + "\n" + sconti[i] + "%");
                }
                else{
                    toggleButton.setText(Mesi.values()[i].toString());
                }
            }
        }
    }

    /**
     * Aggiunge lo sconto, inserito nel campo scontoInput a tutti i mesi selezionati.
     * Se viene inserito uno sconto pari a 0, resetta lo sconto.
     * Aggiorna infine la view, mostrando la tabella di sconti aggiornata
     */
    @FXML
    private void addSconto() {
        int scontoValue = Integer.parseInt(scontoInput.getText());

        //Se lo sconto inserito è valido
        if (scontoValue >= 0 && scontoValue <= 100) {

            // Reset the style if the input is valid
            scontoInput.setStyle("");
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 4; c++) {
                    int i = r * 4 + c;
                    ToggleButton toggleButton = (ToggleButton) selectedMesi.getChildren().get(i);

                    //Se il bottone in posizione i era selezionato, allora aggiungo lo sconto inserito nel mese i
                    if (toggleButton.isSelected()) {
                        sconti[i] = scontoValue;
                        toggleButton.setSelected(false);
                    }
                    if (sconti[i] != 0) {
                        toggleButton.setText(Mesi.values()[i].toString() + "\n" + sconti[i] + "%");
                    }
                }
            }

        }
        //Altrimenti lo segnalo all'utente
        else {
            scontoInput.clear();
            scontoInput.setStyle("-fx-border-color: red;");
            scontoInput.setAccessibleHelp("Inserire un valore tra 0 e 100");
        }
        //Resetto il contenuto degli sconti, aggiornati
        setScontiOutput();
    }

    /**
     * Definisce il comportamento del bottone per l'eliminazione dell'auto.
     */
    @FXML
    private void eliminaAuto(){
        Alert confirmation = new Alert(Alert.AlertType.WARNING);
        confirmation.initStyle(StageStyle.UTILITY);
        confirmation.setTitle("Elimina");
        confirmation.setHeaderText("Sei sicuro di voler eliminare quest'auto?");

        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        //Mostro il popup e attendo che venga premuto
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                catalogoModel.getAllData().remove(catalogoModel.getSelectedAuto());
                goBack();
            }
        });
    }

    /**
     * Salva tutti i valori inseriti all'interno della pagina in tempAuto.
     * Prima di fare ciò si assicura che tutti i campi abbiano un valore valido
     */
    @FXML
    private void addModello() {
        if (isValidStringTextField(modello) &&
                isValidDoubleTextField(false, lunghezza, altezza, larghezza, peso, costoBase) &&
                isValidDoubleTextField(true, volume))
        {
            try {
                //Setto i parametri inseriti all'auto temporanea
                catalogoModel.getTempAuto().setMarca(brand.getValue());
                catalogoModel.getTempAuto().setModello(modello.getText());
                catalogoModel.getTempAuto().setDimensione(new Dimensione(
                        Double.parseDouble(lunghezza.getText()),
                        Double.parseDouble(altezza.getText()),
                        Double.parseDouble(larghezza.getText()),
                        Double.parseDouble(peso.getText()),
                        Double.parseDouble(volume.getText())
                ));
                catalogoModel.getTempAuto().setDescrizione(descrizione.getText());
                catalogoModel.getTempAuto().setCostoBase(Double.parseDouble(costoBase.getText()));
                catalogoModel.getTempAuto().setScontoPerMese(sconti);
                //Carico la pagina successiva
                loadAddImagesPage();
            } catch (Exception e) {
                PageLoader.showErrorPopup("Errore", e.getMessage());
            }
        }
    }



    //NAVIGATORI
    @FXML
    private void loadAddImagesPage() {
        TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage");
        PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addImages.fxml" );
    }

    /**
     * Comportamento del tasto indietro. Viene mostrato un popUp avvisando l'utente
     * che le modifiche effettuate saranno eliminate.
     */
    @FXML
    public void goBack(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.initStyle(StageStyle.UTILITY);
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Sei sicuro di voler abbandonare? Perderai tutte le modifiche");
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        //Mostra il popup di conferma
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                //Resetto tempAuto a null, cancellando tutte le modifiche effettuate
                catalogoModel.setTempAuto(null);
                TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
                PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/catalogoView.fxml" );
            }
        });

    }
}
