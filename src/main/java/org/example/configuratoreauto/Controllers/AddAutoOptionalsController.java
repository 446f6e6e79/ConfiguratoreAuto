package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.configuratoreauto.Controllers.InputValidation.*;


public class AddAutoOptionalsController implements Initializable {
    CatalogoModel catalogo = CatalogoModel.getInstance();
    AutoNuova tempAuto = catalogo.getTempAuto();

    @FXML
    private ChoiceBox<TipoOptional> optionalType;
    @FXML
    private TextField descrizioneOptional;
    @FXML
    private TextField costoOptional;
    @FXML
    private Button addOptional;
    @FXML
    private ScrollPane optionalScrollPane;
    @FXML
    private ChoiceBox<Alimentazione> alimentazioneType;
    @FXML
    private TextField nomeMotore;
    @FXML
    private TextField potenzaMotore;
    @FXML
    private TextField cilindrata;
    @FXML
    private TextField consumi;
    @FXML
    private Button addMotore;
    @FXML
    private ScrollPane motoreScrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Aggiungo i tipi di optional alla choiceBox
        optionalType.getItems().addAll(
                Arrays.stream(TipoOptional.values())
                        .filter(tipo -> tipo != TipoOptional.Colore)
                        .toList()
        );

        //Aggiorno la lista di optional al cambiamento di tipo
        optionalType.setOnAction(t -> updateOptionalList());
        optionalType.setValue(TipoOptional.Interni);

        //Blocco la possibilità di aggiungere un optional fino a che non sono stati valorizzati tutti i campi
        addOptional.disableProperty().bind(
                descrizioneOptional.textProperty().isEmpty()
                        .or(costoOptional.textProperty().isEmpty())
        );

        //Imposto il comportamento del bottone addOptional
        addOptional.setOnAction(t -> addOptional());

        //Limito l'input sul textField costoOptional, in modo che possano essere inseriti solamente numeri double
        costoOptional.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, costoOptional));



        //Aggiungo alla choiceBox tutte le alimentazioni disponibili
        alimentazioneType.getItems().addAll(Alimentazione.values());
        updateMotoriList();
        alimentazioneType.setValue(Alimentazione.BENZINA);

        /*
            Blocco il campo cilindrata nel caso in cui sia selezionata
            alimentazione ELETTRICA. In questo modo, non potranno esserci
            errori di input
         */
        alimentazioneType.setOnAction(t -> {
            if (alimentazioneType.getValue() == Alimentazione.ELETTRICA) {
                cilindrata.setDisable(true);
                cilindrata.setText("0");
            } else {
                cilindrata.setDisable(false);
            }
        });

        //Blocco la possibilità di aggiungere un motore fino a che non sono stati valorizzati tutti i campi
        addMotore.disableProperty().bind(
                nomeMotore.textProperty().isEmpty()
                        .or(potenzaMotore.textProperty().isEmpty()
                        .or(cilindrata.textProperty().isEmpty()
                        .or(consumi.textProperty().isEmpty()))
                        )
        );

        //Imposto il comportamento del tasto addMotore
        addMotore.setOnAction(t-> addMotore());

        //Limito l'input sui campi motore, in modo che possano essere inseriti solo numeri double validi
        potenzaMotore.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidInt(event, potenzaMotore));
        cilindrata.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidInt(event, cilindrata));
        consumi.addEventFilter(KeyEvent.KEY_TYPED, event -> checkValidDouble(event, consumi));
    }

    /**
     * Aggiunge un optional alla lista di TempAuto.
     * Aggiorna inoltre il DISPLAY della lista di optional
     */
    private void addOptional() {
        if(isValidStringTextField(descrizioneOptional) &&
            isValidDoubleTextField(true, costoOptional))
        {
            try {
                tempAuto.addOptional(
                        new Optional(
                                optionalType.getValue(),
                                descrizioneOptional.getText(),
                                Double.parseDouble(costoOptional.getText())
                        ));
                updateOptionalList();
                cleanOptionalFields();
            }
            catch (Exception e){
                PageLoader.showErrorPopup("Errore!", e.getMessage());
            }
        }
    }

    /**
     * Aggiunge, per ogni optional, un elemento che mostra le informazioni per tale optional
     */
    private void updateOptionalList() {
        VBox container = new VBox();
        container.setPrefWidth(optionalScrollPane.getPrefWidth() - 4);
        container.setAlignment(Pos.CENTER);

        List<Optional> optionalsCopy = new ArrayList<>(tempAuto.getOptionalByCategory(optionalType.getValue()));

        for (Optional o : optionalsCopy) {
            HBox optionalElement = new HBox();
            optionalElement.setAlignment(Pos.CENTER);
            optionalElement.setStyle("-fx-border-color: black; -fx-border-width: 2;");
            optionalElement.setPadding(new Insets(10, 10, 10, 10));
            optionalElement.setSpacing(5);
            optionalElement.setPrefWidth(container.getPrefWidth() - 10);
            optionalElement.setPrefHeight(70);

            Text descriptionText = new Text(o.getDescrizione());
            Text costText = new Text(String.valueOf(o.getCosto()));

            ImageView binImg = new ImageView();
            binImg.setFitHeight(50);
            binImg.setFitWidth(50);
            //Rende cliccabile tutto l'imageView
            binImg.setPickOnBounds(true);
            binImg.setImage(new Image(getClass().getResourceAsStream("/img/icons/bin.png")));


            binImg.setOnMouseClicked(event -> {

                tempAuto.getOptionalDisponibili().remove(o);
                updateOptionalList();
            });

            optionalElement.getChildren().addAll(descriptionText, costText, binImg);
            container.getChildren().add(optionalElement);
        }

        optionalScrollPane.setContent(container);
    }

    /**
     * Pulisce i campi di input per l'aggiunta di un optional
     */
    private void cleanOptionalFields(){
        descrizioneOptional.clear();
        costoOptional.clear();
    }

    /**
     * Aggiunge un motore alla lista di TempAuto.
     * Aggiorna inoltre il DISPLAY della lista di motori
     */
    private void addMotore(){
        if(isValidDoubleTextField(false, potenzaMotore, consumi) &&
            isValidDoubleTextField(true, cilindrata)
        ){
            try {
                tempAuto.addMotore(
                        new Motore(
                                nomeMotore.getText(),
                                alimentazioneType.getValue(),
                                Integer.parseInt(potenzaMotore.getText()),
                                Integer.parseInt(cilindrata.getText()),
                                Double.parseDouble(consumi.getText())
                        )
                );
                updateMotoriList();

                //Pulisco i campi di input
                cleanMotoreFields();
            } catch (Exception e) {
                PageLoader.showErrorPopup("Errore!", e.getMessage());
            }
        }
    }

    /**
     * Aggiorna la lista di motori. Crea un elemento per ogni motore,
     * dando la possibilità di eliminarlo
     */
    private void updateMotoriList() {
        VBox container = new VBox();
        container.setPrefWidth(motoreScrollPane.getPrefWidth() - 4);
        container.setAlignment(Pos.CENTER);

        List<Motore> motoriCopy = new ArrayList<>(tempAuto.getMotoriDisponibili());

        for (Motore m : motoriCopy) {
            HBox motoreElement = new HBox();
            motoreElement.setAlignment(Pos.CENTER);
            motoreElement.setStyle("-fx-border-color: black; -fx-border-width: 2;");
            motoreElement.setPadding(new Insets(10, 10, 10, 10));
            motoreElement.setSpacing(5);
            motoreElement.setPrefWidth(container.getPrefWidth() - 10);
            motoreElement.setPrefHeight(70);

            Text nomeMotore = new Text(m.getDescrizione());
            Text alimentazione = new Text(m.getAlimentazione().toString());

            ImageView binImg = new ImageView();
            binImg.setFitHeight(50);
            binImg.setFitWidth(50);
            //Rende cliccabile tutto l'imageView
            binImg.setPickOnBounds(true);
            binImg.setImage(new Image(getClass().getResourceAsStream("/img/icons/bin.png")));

            binImg.setOnMouseClicked(event -> {

                tempAuto.getMotoriDisponibili().remove(m);
                updateMotoriList();
            });

            motoreElement.getChildren().addAll(nomeMotore, alimentazione, binImg);
            container.getChildren().add(motoreElement);
        }

        motoreScrollPane.setContent(container);
    }

    /**
     * Resetta tutti i campi di input riguardanti l'inserimento di un nuovo motore
     */
    private void cleanMotoreFields(){
        nomeMotore.clear();
        potenzaMotore.clear();
        cilindrata.clear();
        consumi.clear();
    }

    @FXML
    public void goBack(){
        TabPane tabPane = (TabPane) consumi.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
        PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/segretarioView/addImages.fxml");
    }

    @FXML
    public void nextPage(){
        //Controllo che sia presente almeno un motore
        if(!tempAuto.getMotoriDisponibili().isEmpty()) {
            //Salvo i dati memorizzati nell'auto temporanea, nel relativo oggetto auto
            catalogo.saveTempData();
            catalogo.setSelectedAuto(null);
            TabPane tabPane = (TabPane) consumi.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            PageLoader.updateTabContent(tabPane, 0, "/org/example/configuratoreauto/catalogoView.fxml");
        }
        else{
            PageLoader.showErrorPopup("Errore!", "Inserisci almeno un motore prima di continuare");
        }
    }
}
