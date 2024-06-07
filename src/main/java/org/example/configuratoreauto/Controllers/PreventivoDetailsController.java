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
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Macchine.TipoOptional;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
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

    private UserModel userModel = UserModel.getInstance();
    private RegistroModel registroModel = RegistroModel.getInstance();

    private Preventivo preventivo;

    public void setPreventivo(Preventivo preventivo) {
        impiegatoButton.setVisible(false);

        //Se l'utente è IMPIEGATO, allora imposto i comportamenti per il bottone
        if(userModel.isImpiegato()){
           impiegatoButton.setVisible(true);

           //Se il preventivo era finalizzato, può settarlo come PAGATO
           if(preventivo.getStato()==StatoPreventivo.FINALIZZATO){
               impiegatoButton.setText("Segnala pagato");
               impiegatoButton.setOnAction(t->setNewStato(StatoPreventivo.PAGATO));
           }

           //Se il preventivo era PAGATO, può settarlo come DISPONIBILE_AL_RITIRO
           if(preventivo.getStato()==StatoPreventivo.PAGATO){
               impiegatoButton.setText("Segnala da ritirare");
               impiegatoButton.setOnAction(t->setNewStato(StatoPreventivo.DISPONIBILE_AL_RITIRO));
           }

           //Se il preventivo era DISPONIBILE_AL_RITIRO, può segnalarlo come RITIRATO
           if(preventivo.getStato()==StatoPreventivo.DISPONIBILE_AL_RITIRO){
               impiegatoButton.setText("Il veicolo è stato ritirato");
               impiegatoButton.setOnAction(t->setNewStato(StatoPreventivo.RITIRATO));
           }
        }
        this.preventivo = preventivo;

        //Carico i dati per l'auto acquistata
        image.setImage(preventivo.getAcquisto().getDefaultImage(null));

        //Genero la tabella con i dettagli sui prezzi
        modello.setText(preventivo.getAcquisto().getModello());
        prezzoBase.setText(preventivo.getAcquisto().getBasePriceAsString());

        //Per ogni optional scelto, aggiungo una riga nella tabella, nel caso fosse il colore aggiorno l'immagine di default
        for(Optional o : preventivo.getOptionalScelti()){
            if(o.getCategoria() == TipoOptional.Colore){
                System.out.println(o.getDescrizione());
                image.setImage(preventivo.getAcquisto().getDefaultImage(o.getDescrizione()));
            }
            addTableRow(o.getDescrizione(), Preventivo.getPriceAsString(o.getCosto()));
        }

        //Aggiunto lo sconto, se presente
        if(preventivo.getScontoAuto() > 0){
            addTableRow("Sconto "+preventivo.getScontoAuto()+"%", preventivo.getScontoAutoFormatted());
        }

        //Se è stato valutato l'usato
        if(preventivo.getValutazione() > 0){
            addTableRow("Valutazione usato", "-"+Preventivo.getPriceAsString(preventivo.getValutazione()));
        }

        //Aggiungo una colonna con il prezzo totale:
        addTableRow("Totale", Preventivo.getPriceAsString(preventivo.getCostoTotale()));

        modelloDescription.setText(preventivo.getAcquisto().getModello());
        marchio.setText(preventivo.getAcquisto().getMarca().toString());
        motore.setText(preventivo.getMotoreScelto().getInfoMotore());
        dimensione.setText(preventivo.getAcquisto().getDimensione().toSimpleString());

        //Carico i dati relativi al preventivo
        cliente.setText(preventivo.getCliente().getName() + " " + preventivo.getCliente().getSurname());
        stato.setText(preventivo.getStato().toString());
        data.setText(preventivo.getDataPreventivoAsString());
        sede.setText(preventivo.getSede().toString());
        //Se il preventivo è già stato finalizzato:
        if(preventivo.getStato()==StatoPreventivo.PAGATO) {
            consegna.setText(preventivo.getDataConsegnaAsString());
            datePreventivo.getChildren().remove(0);
        }else if(preventivo.getStato()==StatoPreventivo.FINALIZZATO){
                scadenza.setText(preventivo.getDataScadenzaAsString());
                datePreventivo.getChildren().remove(1);
            }else{
                datePreventivo.getChildren().clear();
                Text text;
                if(preventivo.getStato() == StatoPreventivo.RICHIESTO) {
                    text = new Text("Attendi che l'usato venga valutato!");
                    text.setStyle("-fx-font-size: 24; -fx-font-weight: bold;-fx-fill: #FFD700");
                }else if(preventivo.getStato() == StatoPreventivo.SCADUTO){
                    text = new Text("Il preventivo risulta scaduto");
                    text.setStyle("-fx-font-size: 24; -fx-font-weight: bold;-fx-fill: #ff0000");
                    datePreventivo.getChildren().add(text);
                }else if(preventivo.getStato() == StatoPreventivo.DISPONIBILE_AL_RITIRO){
                    text = new Text("L'auto è disponibile al ritiro");
                    text.setStyle("-fx-font-size: 24; -fx-font-weight: bold;-fx-fill: #70ff79");
                    datePreventivo.getChildren().add(text);
                }else if(preventivo.getStato() == StatoPreventivo.RITIRATO){
                    text = new Text("L'auto è già stata ritirata");
                    text.setStyle("-fx-font-size: 24; -fx-font-weight: bold;-fx-fill: #7979ff");
                    datePreventivo.getChildren().add(text);
                }
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
            usatoVbox.setVisible(false);
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


    @FXML
    private void goBack() {
        TabPane tabPane = (TabPane) image.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
        PageLoader.updateTabContent(tabPane, 1, "/org/example/configuratoreauto/preventiviView.fxml");
    }

    private void setNewStato(StatoPreventivo newStato){
       preventivo.setStato(newStato);
       registroModel.updateData(preventivo);
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
                contentStream.showText("Data Consegna: " + (preventivo.getStato() == StatoPreventivo.PAGATO ? preventivo.getDataConsegnaAsString() : "da definire"));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Scadenza: " + (preventivo.getStato() == StatoPreventivo.FINALIZZATO ? preventivo.getDataScadenzaAsString() : "da definire"));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Stato: " + preventivo.getStato().toString());
                contentStream.newLineAtOffset(0, -20);

                // Car Details
                contentStream.showText("Modello: " + preventivo.getAcquisto().getModello());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Marchio: " + preventivo.getAcquisto().getMarca().toString());
                contentStream.newLineAtOffset(0, -20);

                // Engine Details
                contentStream.showText("Motore: " + preventivo.getMotoreScelto().getDescrizione());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Alimentazione: " + preventivo.getMotoreScelto().getAlimentazione().toString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Cilindrata: " + preventivo.getMotoreScelto().getCilindrata() + "cc");
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
                if (preventivo.getScontoAuto() != 0) {
                    contentStream.showText(String.format("%-30s %10.2f", "Sconto Applicato(%)", preventivo.getScontoAuto()));
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
