package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.configuratoreauto.Macchine.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddAutoOptionalsController implements Initializable {
    CatalogoModel catalogo = CatalogoModel.getInstance();

    @FXML
    private ChoiceBox<TipoOptional> optionalType;
    @FXML
    private TextField descrizioneOptional;
    @FXML
    private TextField costoOptional;
    @FXML
    private Button addOptional;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        optionalType.getItems().addAll(
                Arrays.stream(TipoOptional.values())
                        .filter(tipo -> tipo != TipoOptional.colore)
                        .toList()
        );
        optionalType.setValue(TipoOptional.cerchi);

        alimentazioneType.getItems().addAll();
        alimentazioneType.setValue(Alimentazione.BENZINA);

        addOptional.disableProperty().bind(
                descrizioneOptional.textProperty().isEmpty()
                .or(costoOptional.textProperty().isEmpty())
        );

        addOptional.setOnAction(t-> {
            addOptional();
        });

        addMotore.setOnAction(t-> {
            addMotore();
        });
    }

    private void addOptional() {
        catalogo.getSelectedAuto().addOptional(
                new Optional(
                        optionalType.getValue(),
                        descrizioneOptional.getText(),
                        Double.parseDouble(costoOptional.getText())
                ));
        updateOptionalList();
    }

    private void updateOptionalList(){

    }

    private void updateMotoriList(){

    }

    private void addMotore(){
        catalogo.getSelectedAuto().addMotore(
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

}
