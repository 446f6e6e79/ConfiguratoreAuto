package org.example.configuratoreauto.Controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.Sede;
import org.example.configuratoreauto.Preventivi.SediModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AutoCustomController implements Initializable {
    CatalogoModel catalogo = CatalogoModel.getInstance();
    SediModel sediModel = SediModel.getInstance();
    AutoNuova auto = catalogo.getSelectedAuto();
    RegistroModel registro = RegistroModel.getInstance();
    UserModel user = UserModel.getInstance();

    private final IntegerProperty currentImageIndex = new SimpleIntegerProperty(-1);
    private final ObservableList<Immagine> imagesCurrentColor = FXCollections.observableArrayList();

    @FXML
    private AnchorPane main;
    @FXML
    private VBox left;
    @FXML
    private VBox right;
    @FXML
    private BorderPane borderPane;

    @FXML
    Text modelID;
    @FXML
    Text descrizione;
    @FXML
    private ImageView images;
    @FXML
    Text prezzo;
    @FXML
    Text infoPrezzo;
    @FXML
    ChoiceBox<Motore> motori;
    @FXML
    ChoiceBox<Optional> colori;
    @FXML
    ChoiceBox<Optional> interni;
    @FXML
    ChoiceBox<Optional> vetri;
    @FXML
    ChoiceBox<Optional> cerchi;
    @FXML
    Text motoreInfo;
    @FXML
    Text dimensioni;
    @FXML
    Text valido;
    @FXML
    ChoiceBox<Sede> sedi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //============RESIZING
        borderPane.prefWidthProperty().bind(main.widthProperty());
        borderPane.prefHeightProperty().bind(main.heightProperty());
        left.prefWidthProperty().bind(borderPane.widthProperty().divide(2));
        right.prefWidthProperty().bind(borderPane.widthProperty().divide(2));
        images.fitWidthProperty().bind(left.prefWidthProperty());
        //================================

        // Carica la prima immagine
        updateImage();

        modelID.setText(auto.getModello());
        String dim = auto.getDimensione().toString();
        dimensioni.setText(dim);
        descrizione.setText(auto.getDescrizione());
        String price = Preventivo.getPriceAsString(auto.getCostoTotale(new ArrayList<>(), new Date()));
        prezzo.setText(price);
        motori.getItems().addAll(auto.getMotoriDisponibili());
        updatePriceInfo(new ArrayList<>());

        motori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                motoreInfo.setText(newValue.getInfoMotore());
            }
        });
        motori.setValue(motori.getItems().get(0));
        colori.getItems().addAll(auto.getOptionalByCategory(TipoOptional.colore));
        interni.getItems().addAll(auto.getOptionalByCategory(TipoOptional.interni));
        vetri.getItems().addAll(auto.getOptionalByCategory(TipoOptional.vetri));
        cerchi.getItems().addAll(auto.getOptionalByCategory(TipoOptional.cerchi));
        sedi.getItems().addAll(sediModel.getAllData());

        ChangeListener<Optional> priceUpdateListener = (observable, oldValue, newValue) -> getDynamicPrice();
        colori.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        interni.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        vetri.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        cerchi.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);

        colori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateImagesForColor(String.valueOf(newValue)));
    }


    private void updateImagesForColor(String color) {
        imagesCurrentColor.clear();
        for (Immagine img : catalogo.getSelectedAuto().getImmagini()) {
            if (img.getColor().equals(color)) {
                imagesCurrentColor.add(img);
            }
        }
        currentImageIndex.setValue(0);
    }

    //Aggiorna la ImageView alla foto sucessiva, aggiornando inoltre l'index
    public void prevImage() {
        if (currentImageIndex.get() < imagesCurrentColor.size() - 1) {
            currentImageIndex.set(currentImageIndex.get() + 1);
            images.setImage(imagesCurrentColor.get(currentImageIndex.get()).getImage());
        }
    }

    //Aggiorna la ImageView alla foto precedente
    public void nextImage() {
        if (currentImageIndex.get() > 0) {
            currentImageIndex.set(currentImageIndex.get() - 1);
            images.setImage(imagesCurrentColor.get(currentImageIndex.get()).getImage());
        }
    }

    public void resetChoices(){
        colori.setValue(null);
        interni.setValue(null);
        vetri.setValue(null);
        cerchi.setValue(null);
        sedi.setValue(null);
    }

    public void createPreventivo() {
        ArrayList<Optional> chosen = new ArrayList<>();
        if(motori.getValue()!=null && sedi.getValue()!=null){
            valido.setText("");
            if(user.getCurrentUser() == null){
                openRegistratiView();
            }
            Preventivo p = new Preventivo(auto, sedi.getValue(), (Cliente) user.getCurrentUser(), motori.getValue(), chosen);
            registro.currentPreventivo = p;
            openUsataView();
            System.out.println("INSERITO PREVENTIVO");
            this.backClicked();
        }else{
            valido.setText("Non hai inserito i campi correttamente\n");
        }
    }

    public void getDynamicPrice() {
        ArrayList<Optional> selezionati = new ArrayList<>();
        selezionati.add(colori.getValue());
        selezionati.add(interni.getValue());
        selezionati.add(vetri.getValue());
        selezionati.add(cerchi.getValue());
        double costoTotale =auto.getCostoTotale(selezionati, new Date());
        updatePriceInfo(selezionati);
        prezzo.setText(Preventivo.getPriceAsString(costoTotale));
    }

    public void updatePriceInfo(ArrayList<Optional> selezionati) {
        String s= "Costo base: "+auto.getBasePriceAsString();

        for(Optional o : selezionati) {
            s+= o.toString() + "\n";
        }
        String sconto = "\nSconto applicato: "+ auto.getSconto(new Date()) +"%";
        s+=sconto;
        infoPrezzo.setText(s);
    }

    @FXML
    public void backClicked(){
        try {
            TabPane tabPane = (TabPane) modelID.getScene().lookup("#mainPage");
            Tab tab= tabPane.getTabs().get(0);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
            AnchorPane catalogoNode;
            BorderPane preventiviNode;
            catalogoNode = loader.load();
            loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            preventiviNode = loader.load();
            PreventiviController contr = loader.getController();
            contr.loadPrevs(registro.getPreventiviByCliente((Cliente) user.getCurrentUser()));
            Tab tab1 = tabPane.getTabs().get(1);
            tab1.setContent(preventiviNode);
            tab.setContent(catalogoNode);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void openRegistratiView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/loginPage.fxml"));
            VBox loginPane = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle("Registrati/Login");
            Scene scene = new Scene(loginPane);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(main.getScene().getWindow());
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUsataView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/usataCustom.fxml"));
            BorderPane usataView = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle("Auto Usata");
            Scene scene = new Scene(usataView);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(main.getScene().getWindow());
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
