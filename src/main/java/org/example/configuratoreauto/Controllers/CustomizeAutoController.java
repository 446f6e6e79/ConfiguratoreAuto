package org.example.configuratoreauto.Controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.Sede;
import org.example.configuratoreauto.Preventivi.SediModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;
import java.net.URL;
import java.util.*;

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
    private VBox optionalList;
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
        updatePriceTableInfo();
        sedi.getItems().addAll(sediModel.getAllData());

        //Alla selezione del motore, aggiorno le informazioni
        motori.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                motoreInfo.setText(newValue.getInfoMotore());
            }
        });
        motori.setValue(motori.getItems().get(0));


        //Creo una lista delle choiceBox per gli optional
        List<ChoiceBox> choiceBoxes = Arrays.asList(colori, interni, vetri, cerchi);
        int addedOptional = 1;

        ArrayList<Optional> optionalByType;
        for(TipoOptional tO: TipoOptional.values()) {
            //Recupero tutti gli optional per quella data categoria
            optionalByType = auto.getOptionalByCategory(tO);

            //Se sono presenti degli optional per quel tipo
            if(!optionalByType.isEmpty()) {
                addedOptional++;
                //Recupero la choicheBox per quell'optional
                ChoiceBox currCB = choiceBoxes.get(tO.ordinal());
                //Aggiungo gli optional disponibili per quel tipo
                currCB.getItems().addAll(auto.getOptionalByCategory(tO));
            }
            else{
                choiceBoxes.remove(tO);
                //Rimuovo l'elemento per quel tipo di optional
                optionalList.getChildren().remove(addedOptional);
            }
        }

        /*
        * TODO: una volta aggiunti i colori ad ogni auto, l'if può essere rimosso
        * */
        if (!colori.getItems().isEmpty()) {
            colori.setValue(colori.getItems().get(0));
        }

        /*
        *   Imposto i listener per l'aggiornamento dinamico del prezzo.
        *   Ogni qualvolta vienew aggiornato un optional, di conseguenza è aggiornata
        *   la tabella con la descrizione dei costi
        * */
        ChangeListener<Optional> priceUpdateListener = (observable, oldValue, newValue) -> updatePriceTableInfo();
        colori.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        interni.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        vetri.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);
        cerchi.getSelectionModel().selectedItemProperty().addListener(priceUpdateListener);

        //Al cambiamento della selezione del colore, sono aggiornate dinamicamente le immagini
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
    }



    public void createPreventivo() {

        ArrayList<Optional> chosen = getChoseOptional();
        if(motori.getValue()!=null && sedi.getValue()!=null){
            if(user.getCurrentUser() == null){
                openRegistratiView();
                if(user.getCurrentUser() == null){
                    return;
                }
            }
            valido.setText("");
            //Creo una copia per scollegare il preventivo dall'auto
            AutoNuova copia = new AutoNuova(auto);
            registro.setCurrentPreventivo(new Preventivo(copia, sedi.getValue(), (Cliente) user.getCurrentUser(), motori.getValue(), chosen));
            openUsataView();
            System.out.println("FINIO");
            catalogo.setSelectedAuto(null);
            goBack();
        }else{
            PageLoader.showErrorPopup("Errore", "Non hai inserito i campi obbligatori");
        }
    }

    private ArrayList<Optional> getChoseOptional() {
        ArrayList<Optional> chosen = new ArrayList<>();
        if(colori.getValue()!=null)
            chosen.add(colori.getValue());
        if(interni.getValue()!=null)
            chosen.add(interni.getValue());
        if(vetri.getValue()!=null)
            chosen.add(vetri.getValue());
        if(cerchi.getValue()!=null)
            chosen.add(cerchi.getValue());
        return chosen;
    }

    /**
     *  Aggiorna la tabella dei prezzi attuali, generando dinamicamente il prezzo
     */
    private void updatePriceTableInfo() {
        //Recupero gli optional selezionati
        ArrayList<Optional> chosen = getChoseOptional();

        //Rimuovo gli optional precedenti
        if (priceDetails.getChildren().size() > 2) {
            priceDetails.getChildren().subList(2, priceDetails.getChildren().size()).clear();
        }

        //Genero dinamicamente la nuova tabella del prezzo
        for(Optional o : chosen) {
            if(o!=null){
                addTableRow(o.getDescrizione(), Preventivo.getPriceAsString(o.getCosto()));
            }
        }

        int sconto = auto.getSconto(new Date());
        double prezzoTotale = auto.getCostoTotale(chosen, new Date());

        //Se è presente uno sconto
        if(sconto > 0){
            addTableRow("Sconto "+auto.getSconto(new Date())+"%", Preventivo.getPriceAsString(prezzoTotale - auto.getPrezzoNoSconto(chosen)));
        }
        addTableRow("Totale", Preventivo.getPriceAsString(prezzoTotale));
    }

    @FXML
    public void goBack(){
        TabPane tabPane = (TabPane) modelID.getScene().lookup("#mainPage");
        PageLoader.updateTabContent(tabPane, 0,"/org/example/configuratoreauto/catalogoView.fxml");
        PageLoader.updateTabContent(tabPane, 1,"/org/example/configuratoreauto/preventiviView.fxml");

    }

    private void openRegistratiView() {
        PageLoader.openDialog("/org/example/configuratoreauto/loginPage.fxml", "Registrati/Login", main);
        TabPane tabPane = (TabPane) modelID.getScene().lookup("#mainPage");
        Tab logout = tabPane.getTabs().get(2);
        logout.setText("Logout");
        logout.setStyle("-fx-background-color: ffcccc;");
    }

    public void openUsataView() {
        PageLoader.openDialog("/org/example/configuratoreauto/clienteView/addAutoUsata.fxml", "Auto Usata", main);
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
