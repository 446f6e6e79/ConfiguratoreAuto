package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


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

        optionalType.getItems().addAll(
                Arrays.stream(TipoOptional.values())
                        .filter(tipo -> tipo != TipoOptional.Colore)
                        .toList()
        );
        optionalType.setOnAction(t-> updateOptionalList());
        optionalType.setValue(TipoOptional.Cerchi);

        addOptional.disableProperty().bind(
                descrizioneOptional.textProperty().isEmpty()
                        .or(costoOptional.textProperty().isEmpty())
        );
        addOptional.setOnAction(t-> {
            addOptional();
        });

        costoOptional.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            checkValidDouble(event, costoOptional);
        });



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

        addMotore.setOnAction(t-> {
            addMotore();
        });
    }

    /**
     * Aggiunge un optional alla lista di TempAuto.
     * Aggiorna inoltre il DISPLAY della lista di optional
     */
    private void addOptional() {
        tempAuto.addOptional(
                new Optional(
                        optionalType.getValue(),
                        descrizioneOptional.getText(),
                        Double.parseDouble(costoOptional.getText())
                ));
        updateOptionalList();
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
     * Aggiunge un motore alla lista di TempAuto.
     * Aggiorna inoltre il DISPLAY della lista di motori
     */
    private void addMotore(){
        tempAuto.addMotore(
                new Motore(
                        nomeMotore.getText(),
                        alimentazioneType.getValue(),
                        Integer.parseInt(potenzaMotore.getText()),
                        Integer.parseInt(cilindrata.getText()),
                        Integer.parseInt(consumi.getText())
                )
        );
        updateMotoriList();
    }

    private void updateMotoriList() {
        VBox container = new VBox();
        container.setPrefWidth(optionalScrollPane.getPrefWidth() - 4);
        container.setAlignment(Pos.CENTER);

        List<Motore> motoriCopy = new ArrayList<>(tempAuto.getMotoriDisponibili());

        for (Motore m : motoriCopy) {
            HBox optionalElement = new HBox();
            optionalElement.setAlignment(Pos.CENTER);
            optionalElement.setStyle("-fx-border-color: black; -fx-border-width: 2;");
            optionalElement.setPadding(new Insets(10, 10, 10, 10));
            optionalElement.setSpacing(5);
            optionalElement.setPrefWidth(container.getPrefWidth() - 10);
            optionalElement.setPrefHeight(70);

            Text nomeMotore = new Text(m.getNome());
            Text alimentazione = new Text(m.getAlimentazione().toString());

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

            optionalElement.getChildren().addAll(nomeMotore, alimentazione, binImg);
            container.getChildren().add(optionalElement);
        }

        optionalScrollPane.setContent(container);
    }

    @FXML
    public void goBack(){
        try {
            TabPane tabPane = (TabPane) consumi.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/segretarioView/addImages.fxml"));
            AnchorPane modelNode;
            modelNode = loader.load();
            tab.setContent(modelNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void nextPage(){
        //Salvo i dati memorizzati nell'auto temporanea, nel relativo oggetto auto
        catalogo.saveTempData();
        try {
            catalogo.setSelectedAuto(null);
            TabPane tabPane = (TabPane) consumi.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(0); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
            AnchorPane catalogoNode;

            catalogoNode = loader.load();
            tab.setContent(catalogoNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }
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
