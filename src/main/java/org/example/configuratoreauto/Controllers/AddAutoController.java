package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAutoController implements Initializable {
    @FXML
    private TextArea modello;
    @FXML
    private FlowPane inputImages = new FlowPane();

    CatalogoModel catalogo = CatalogoModel.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sto aggiungendo una nuova auto
        if(catalogo.getSelectedAuto() == null){
            inputImages.getChildren().clear();
            for(int i = 0; i < 4; i++){
                addInputImageElement();
            }
        }
        //Sto modificando un auto già presente
        else{

        }
    }
    @FXML
    private void addImageFile(ImageView imageView){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona l'immagine");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png"),
                new FileChooser.ExtensionFilter("Image files", "*.jpg")
        );

        File imageFile = fileChooser.showOpenDialog(new Stage());
        //Gestione delle immagini
        if(imageFile != null){
            addInputImageElement();
            Image image = new Image(imageFile.toURI().toString());
            imageView.setImage(image);
        }
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

    private void addInputImageElement() {
        HBox imageContainer = new HBox();
        ImageView imageView = new ImageView();
        imageContainer.setOnMouseClicked(t -> addImageFile(imageView));

        imageView.setFitHeight(170);
        imageView.setFitWidth(170);
        Image image = new Image(getClass().getResourceAsStream("/img/icons/addImage.png"));
        imageView.setImage(image);
        imageContainer.getChildren().add(imageView);

        inputImages.getChildren().add(0, imageContainer);
    }

}
