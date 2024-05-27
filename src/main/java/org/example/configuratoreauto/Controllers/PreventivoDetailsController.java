package org.example.configuratoreauto.Controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

public class PreventivoDetailsController {

    @FXML
    private ImageView image;
    @FXML
    private Label modello;
    @FXML
    private VBox priceDetails;
    @FXML
    private Label prezzoBase;
    @FXML
    private Label marchio;
    @FXML
    private Label modelloDescription;
    @FXML
    private Label cliente;
    @FXML
    private Label sede;
    @FXML
    private Label data;
    @FXML
    private HBox datePreventivo;
    @FXML
    private Label consegna;
    @FXML
    private Label scadenza;
    @FXML
    private Label stato;
    @FXML
    private Label costo;
    @FXML
    private Label motore;
    @FXML
    private Label dimensione;
    @FXML
    private Label usatoMarchio;
    @FXML
    private Label usatoModello;
    @FXML
    private Label kmResult;
    @FXML
    private Label targa;
    @FXML
    private VBox usatoVbox;
    @FXML
    private Button impiegatoButton;

    private UserModel user = UserModel.getInstance();
    private RegistroModel registro = RegistroModel.getInstance();

    private Preventivo preventivo;

    public void setPreventivo(Preventivo preventivo) {
        impiegatoButton.setVisible(false);

        if(user.getCurrentUser() instanceof Impiegato){
           impiegatoButton.setVisible(true);
           if(preventivo.getStato()==StatoPreventivo.FINALIZZATO){
               impiegatoButton.setText("Segnala pagato");
           }
           if(preventivo.getStato()==StatoPreventivo.PAGATO){
               impiegatoButton.setText("Segnala da ritirare");
           }
           if(preventivo.getStato()==StatoPreventivo.DISPONIBILE_AL_RITIRO){
               impiegatoButton.setText("Il veicolo è stato ritirato");
           }
        }
        this.preventivo = preventivo;

        /*
        *   Carico i dati per l'auto acquistata
        *   TODO: recuperare il colore selezionato, in modo da personalizzare l'immagine;
        * */
        image.setImage(preventivo.getAcquisto().getDefaultImage(null));
        modello.setText(preventivo.getAcquisto().getModello());
        prezzoBase.setText(preventivo.getAcquisto().getBasePriceAsString());

        //Per ogni optional scelto, aggiungo una riga nella tabella
        for(Optional o : preventivo.getOptionalScelti()){
            addTableRow(o.getDescrizione(), Preventivo.getPriceAsString(o.getCosto()));
        }

        //Aggiunto lo sconto, se presente
        if(preventivo.getAcquisto().getSconto(preventivo.getData()) > 0){
            addTableRow("Sconto", "TO DO: GENERA SCONTO");
        }

        //Aggiungo una colonna con il prezzo totale:
        addTableRow("Totale", Preventivo.getPriceAsString(preventivo.getCostoTotale()));

        modelloDescription.setText(preventivo.getAcquisto().getModello());
        marchio.setText(preventivo.getAcquisto().getMarca().toString());
        motore.setText(preventivo.getMotoreScelto().getInfoMotore());
        dimensione.setText(preventivo.getAcquisto().getDimensione().toSimpleString());

        /*
        *   Carico i dati relativi al preventivo
        * */
        cliente.setText(preventivo.getCliente().getName() + " " + preventivo.getCliente().getSurname());
        stato.setText(preventivo.getStato().toString());
        data.setText(preventivo.getDataPreventivoAsString());
        sede.setText(preventivo.getSede().toString());

        //Se il preventivo è già stato finalizzato:
        if(preventivo.getStato()!=StatoPreventivo.RICHIESTO) {
            consegna.setText(preventivo.getDataConsegnaAsString());
            scadenza.setText(preventivo.getDataScadenzaAsString());

        }
        else{
            datePreventivo.getChildren().clear();
            Text text = new Text("Attendi che l'usato venga valutato!");
            text.setStyle("-fx-font-size: 24; -fx-font-weight: bold;-fx-fill: #FFD700");
            datePreventivo.getChildren().add(text);
        }

        /*
        *   Aggiungo le informazioni per l'auto ussata, se presenti
        * */
        if (preventivo.getUsata() != null) {
            usatoMarchio.setText(preventivo.getUsata().getMarca().toString());
            usatoModello.setText(preventivo.getUsata().getModello());
            kmResult.setText(String.valueOf(preventivo.getUsata().getKm()));
            targa.setText(preventivo.getUsata().getTarga());
        }
        //Se non è presente un auto usata, elimino l'elemento
        else {
            usatoVbox.getChildren().clear();
        }
        String s = "";
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


    @FXML
    private void goBack() {
        try {
            TabPane tabPane = (TabPane) image.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(1); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            BorderPane preventiviList;
            preventiviList = loader.load();
            PreventiviController controller = loader.getController();
            if(user.getCurrentUser() instanceof Cliente){
                controller.loadPrevs(registro.getPreventiviByCliente((Cliente) user.getCurrentUser()));
            }else if(user.getCurrentUser() instanceof Segretario){
                controller.loadPrevs(registro.getAllData());
            }else if(user.getCurrentUser() instanceof Impiegato && preventivo.getStato()!=StatoPreventivo.RITIRATO){
                controller.loadPrevs(registro.getPreventiviByStato(preventivo.getStato()));
            }else if(user.getCurrentUser() instanceof Impiegato && preventivo.getStato()==StatoPreventivo.RITIRATO){
                controller.loadPrevs((registro.getPreventiviByStato(StatoPreventivo.FINALIZZATO)));
            }
            tab.setContent(preventiviList); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void impiegatoPress(){
        preventivo.setStato(StatoPreventivo.PAGATO);
        if(user.getCurrentUser() instanceof Impiegato){

            if(preventivo.getStato()==StatoPreventivo.FINALIZZATO){
                preventivo.setStato(StatoPreventivo.PAGATO);
            }
            else if(preventivo.getStato()==StatoPreventivo.PAGATO){
                preventivo.setStato(StatoPreventivo.DISPONIBILE_AL_RITIRO);
            }
            else if(preventivo.getStato()==StatoPreventivo.DISPONIBILE_AL_RITIRO){
                preventivo.setStato(StatoPreventivo.RITIRATO);
            }
            registro.updateData(preventivo);
        }
        goBack();
    }



    @FXML
    private void onPdf() {
        // Create a new PDF document
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Create content for the PDF page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Header
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("PREVENTIVO CONCESSIONARIO AUTO");
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(0, -40);

                // Customer Details
                contentStream.showText("Cliente: " + preventivo.getCliente());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Sede: " + preventivo.getSede().getNome() + " " + preventivo.getSede().getIndirizzo());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Data Preventivo: " + preventivo.getDataPreventivoAsString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Data Consegna: " + (preventivo.getStato() != StatoPreventivo.RICHIESTO ? preventivo.getDataConsegnaAsString() : "da definire"));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Scadenza: " + (preventivo.getStato() != StatoPreventivo.RICHIESTO ? preventivo.getDataScadenzaAsString() : "da definire"));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Stato: " + preventivo.getStato().toString());
                contentStream.newLineAtOffset(0, -20);

                // Car Details
                contentStream.showText("Modello: " + preventivo.getAcquisto().getModello());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Marchio: " + preventivo.getAcquisto().getMarca().toString());
                contentStream.newLineAtOffset(0, -20);

                // Engine Details
                contentStream.showText("Motore: " + preventivo.getMotoreScelto().getNome());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Alimentazione: " + preventivo.getMotoreScelto().getAlimentazione().toString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Cilindrata: " + preventivo.getMotoreScelto().getCilindrata() + "cc");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Cavalli: " + preventivo.getMotoreScelto().getCavalli() + "HP");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Potenza: " + preventivo.getMotoreScelto().getPotenzaKW() + "kW");
                contentStream.newLineAtOffset(0, -20);

                // Dimension Details
                contentStream.showText("Dimensioni:");
                contentStream.newLineAtOffset(20, -20);
                contentStream.showText("Altezza: " + preventivo.getAcquisto().getDimensione().getAltezza() + "m");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Larghezza: " + preventivo.getAcquisto().getDimensione().getLarghezza() + "m");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Lunghezza: " + preventivo.getAcquisto().getDimensione().getLunghezza() + "m");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Peso: " + preventivo.getAcquisto().getDimensione().getPeso() + "kg");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Volume del Bagagliaio: " + preventivo.getAcquisto().getDimensione().getVolumeBagagliaglio() + "l");
                contentStream.newLineAtOffset(-20, -20);

                // Used Car Details
                if (preventivo.getUsata() != null) {
                    AutoUsata usata = preventivo.getUsata();
                    contentStream.showText("Auto usata:");
                    contentStream.newLineAtOffset(20, -20);
                    contentStream.showText("Modello/Marca: " + usata.getModello() + " " + usata.getMarca());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Targa: " + usata.getTarga());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Km: " + usata.getKm());
                    contentStream.newLineAtOffset(-20, -20);
                }

                // Cost Details Table Header
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 15);
                contentStream.showText("COSTO DETTAGLIATO");
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(0, -20);

                // Cost Details Table
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.showText(String.format("%-30s %10s", "Descrizione", "Costo (€)"));
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.showText(String.format("%-30s %10.2f", "Costo Base", preventivo.getAcquisto().getCostoBase()));
                contentStream.newLineAtOffset(0, -20);
                if (preventivo.getAcquisto().getSconto(preventivo.getData()) != 0) {
                    contentStream.showText(String.format("%-30s %10.2f", "Sconto Mensile", preventivo.getAcquisto().getSconto(preventivo.getData())));
                    contentStream.newLineAtOffset(0, -20);
                }
                contentStream.showText("Costi Optionals:");
                contentStream.newLineAtOffset(0, -20);

                int c = 0;
                for (Optional opt : preventivo.getOptionalScelti()) {
                    if (opt != null) {
                        c++;
                        contentStream.showText(String.format("%-30s %10.2f", opt.getDescrizione(), opt.getCosto()));
                        contentStream.newLineAtOffset(0, -20);
                    }
                }
                if (c == 0) {
                    contentStream.showText("   Nessun optional");
                    contentStream.newLineAtOffset(0, -20);
                }
                if (preventivo.getStato() != StatoPreventivo.RICHIESTO && preventivo.getUsata() != null) {
                    contentStream.showText(String.format("%-30s %10.2f", "Valutazione dell'usato", preventivo.getValutazione()));
                    contentStream.newLineAtOffset(0, -20);
                }

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.showText(String.format("%-30s %10.2f", "Costo totale", preventivo.getCostoTotale()));
                contentStream.endText();
            }

            // Save the PDF document to disk
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            File file = fileChooser.showSaveDialog(modello.getScene().getWindow());

            if (file != null) {
                document.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
