package org.example.configuratoreauto.Controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
import java.util.ResourceBundle;

public class CustomizeAutoController implements Initializable {
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
    private Text modelID;
    @FXML
    private Text descrizione;
    @FXML
    private ImageView images;
    @FXML
    private VBox priceDetails;
    @FXML
    private Label prezzoBase;
    @FXML
    private Label modello;
    @FXML
    private ChoiceBox<Motore> motori;
    @FXML
    private ChoiceBox<Optional> colori;
    @FXML
    private ChoiceBox<Optional> interni;
    @FXML
    private ChoiceBox<Optional> vetri;
    @FXML
    private ChoiceBox<Optional> cerchi;
    @FXML
    private Text motoreInfo;
    @FXML
    private Text dimensioni;
    @FXML
    private Text valido;
    @FXML
    private ChoiceBox<Sede> sedi;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* Imposto il valore dei campi di base */
        modelID.setText(auto.getModello());
        modello.setText(auto.getModello());
        prezzoBase.setText(auto.getBasePriceAsString());
        String dim = auto.getDimensione().toString();
        dimensioni.setText(dim);
        descrizione.setText(auto.getDescrizione());
        motori.getItems().addAll(auto.getMotoriDisponibili());
        updatePriceTableInfo(new ArrayList<>());


        motori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                motoreInfo.setText(newValue.getInfoMotore());
            }
        });
        motori.setValue(motori.getItems().get(0));
        colori.getItems().addAll(auto.getOptionalByCategory(TipoOptional.Colore));

        interni.getItems().add(null);
        interni.getItems().addAll(auto.getOptionalByCategory(TipoOptional.Interni));

        vetri.getItems().add(null);
        vetri.getItems().addAll(auto.getOptionalByCategory(TipoOptional.Vetri));

        cerchi.getItems().add(null);
        cerchi.getItems().addAll(auto.getOptionalByCategory(TipoOptional.Cerchi));
        sedi.getItems().addAll(sediModel.getAllData());

        if (!colori.getItems().isEmpty()) {
            colori.setValue(colori.getItems().get(0));
        }

        /*
        *   Imposto i listener per l'aggiornamento dinamico del prezzo.
        *   Ogni qualvolta vienew aggiornato un optional, di conseguenza è aggiornata
        *   la tabella con la descrizione dei costi
        * */
        ChangeListener<Optional> priceUpdateListener = (observable, oldValue, newValue) -> getDynamicPrice();
        colori.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        interni.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        vetri.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        cerchi.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);

        colori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateImagesForColor(newValue.getDescrizione()));
        if(colori.getValue()!=null)
            updateImagesForColor(colori.getValue().getDescrizione());
    }


    private void updateImagesForColor(String color) {
        imagesCurrentColor.clear();

        for (Immagine img : catalogo.getSelectedAuto().getImmagini()) {
            System.out.println(img.getColor());
            if (img.getColor().equals(color)) {
                imagesCurrentColor.add(img);
            }
        }
        if (!imagesCurrentColor.isEmpty()) {
            currentImageIndex.setValue(0);
            updateImage();
        } else {
            System.out.println("No images available for the selected color: " + color);
        }
    }

    /**
     * Aggiorna l'imageView, caricando la foto puntata da currentImageIndex
     */
    private void updateImage() {
        if (!imagesCurrentColor.isEmpty()) {
            images.setImage(imagesCurrentColor.get(currentImageIndex.get()).getImage());
        }
    }

    /**
     * Aggiorna l'indice in modo da mostrare l'immagine precedente. Si distinguono due casi:
     *  - se currentImageIndex == 0 -> viene impostato all'ultima foto
     *  - altrimenti viene decrementato di 1
     */
    public void prevImage() {
        System.out.println(modelID.getStyle());
        if (currentImageIndex.get() > 0) {
            currentImageIndex.set(currentImageIndex.get() - 1);
        } else {
            currentImageIndex.set(imagesCurrentColor.size() - 1);
        }
        updateImage();
    }

    /**
     * Aggiorna l'indice in modo da mostrare l'immagine successiva. Si distinguono due casi:
     *  - se currentImageIndex == size - 1 -> viene impostato alla prima foto
     *  - altrimenti viene incrementato di 1
     */
    public void nextImage() {
        if (currentImageIndex.get() < imagesCurrentColor.size() - 1) {
            currentImageIndex.set(currentImageIndex.get() + 1); // Increment index to move to the next image
        } else {
            currentImageIndex.setValue(0); // Wrap around to the first image
        }
        updateImage();
    }

    /**
     * Resetta tutte le ChoiceBox presenti nella pagina
     */
    public void resetChoices(){
        colori.setValue(colori.getItems().get(0));
        currentImageIndex.set(0);
        interni.setValue(null);
        vetri.setValue(null);
        cerchi.setValue(null);
        sedi.setValue(null);
    }

    public void createPreventivo() {
        ArrayList<Optional> chosen = new ArrayList<>();
        if(colori.getValue()!=null)
            chosen.add(colori.getValue());
        if(interni.getValue()!=null)
            chosen.add(interni.getValue());
        if(vetri.getValue()!=null)
            chosen.add(vetri.getValue());
        if(cerchi.getValue()!=null)
            chosen.add(cerchi.getValue());
        if(motori.getValue()!=null && sedi.getValue()!=null){
            valido.setText("");
            if(user.getCurrentUser() == null){
                openRegistratiView();
            }
            registro.currentPreventivo = new Preventivo(auto, sedi.getValue(), (Cliente) user.getCurrentUser(), motori.getValue(), chosen);
            openUsataView();
            catalogo.setSelectedAuto(null);
            goBack();
        }else{
            valido.setText("Non hai inserito i campi correttamente\n");
        }
    }

    /**
     * Genera dinamicamente il prezzo.
     * Recupera gli optional scelti, aggiornando la tabella dei prezzi dinamicamente
     */
    public void getDynamicPrice() {
        ArrayList<Optional> selezionati = new ArrayList<>();
        if(colori.getValue()!=null)
            selezionati.add(colori.getValue());
        if(interni.getValue()!=null)
            selezionati.add(interni.getValue());
        if(vetri.getValue()!=null)
            selezionati.add(vetri.getValue());
        if(cerchi.getValue()!=null)
            selezionati.add(cerchi.getValue());
        updatePriceTableInfo(selezionati);
        
    }

    /**
     *  Aggiorna la tabella dei prezzi attuali
     * @param selezionati Lista di optional selezionati
     */
    private void updatePriceTableInfo(ArrayList<Optional> selezionati) {

        //Rimuovo gli optional precedenti
        if (priceDetails.getChildren().size() > 2) {
            priceDetails.getChildren().subList(2, priceDetails.getChildren().size()).clear();
        }

        //Genero dinamicamente la nuova tabella del prezzo
        for(Optional o : selezionati) {
            if(o!=null){
                addTableRow(o.getDescrizione(), Preventivo.getPriceAsString(o.getCosto()));
            }
        }

        int sconto = auto.getSconto(new Date());
        double prezzoTotale = auto.getCostoTotale(selezionati, new Date());
        //Se è presente uno sconto
        if(sconto > 0){
            addTableRow("Sconto "+auto.getSconto(new Date())+"%", Preventivo.getPriceAsString(prezzoTotale - auto.getPrezzoNoSconto(selezionati, new Date())));
        }
        addTableRow("Costo totale:", Preventivo.getPriceAsString(prezzoTotale));
    }

    @FXML
    public void goBack(){
        try {
            TabPane tabPane = (TabPane) modelID.getScene().lookup("#mainPage");
            Tab tab= tabPane.getTabs().get(0);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/catalogoView.fxml"));
            BorderPane catalogoNode;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/addAutoUsata.fxml"));
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

    private void addTableRow(String description, String price) {
        HBox row = new HBox();
        row.getStyleClass().add("tableRow");

        // Creo la label per la descrizione dell'optional
        Label descriptionLabel = new Label(description);
        descriptionLabel.setAlignment(Pos.CENTER);
        descriptionLabel.getStyleClass().add("tableRowLabel");
        descriptionLabel.setPrefWidth(prezzoBase.getPrefWidth());

        // Creo la label per il prezzo dell'optional
        Label priceLabel = new Label(price);
        priceLabel.setAlignment(Pos.CENTER);
        priceLabel.getStyleClass().add("tableRowLabel");
        priceLabel.prefWidthProperty().bind(descriptionLabel.widthProperty());

        // Aggiungo gli elementi alla riga, aggiungo la riga alla tabella
        row.getChildren().addAll(descriptionLabel, priceLabel);
        priceDetails.getChildren().add(row);
    }
}
