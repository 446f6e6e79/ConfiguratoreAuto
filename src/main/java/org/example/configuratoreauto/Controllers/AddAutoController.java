package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

import java.io.IOException;

public class AddAutoController {
    @FXML
    private TextArea modello;
    @FXML
    private void addImageFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.png"),
                new FileChooser.ExtensionFilter("Text Files", "*.jpg")
        );
        File immagine = fileChooser.showOpenDialog(new Stage());
        System.out.println(immagine.getAbsolutePath());
    }
    public void backClicked(){
        try {
            TabPane tabPane = (TabPane) modello.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
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
