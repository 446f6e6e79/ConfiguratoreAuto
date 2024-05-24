package org.example.configuratoreauto.Controllers;

import javafx.scene.image.ImageView;
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
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

public class PreventivoDetailsController {

    @FXML
    private Label cliente;

    @FXML
    private Label sede;

    @FXML
    private Label data;

    @FXML
    private Label consegna;

    @FXML
    private Label scadenza;

    @FXML
    private Label stato;

    @FXML
    private Label costo;

    @FXML
    private Label modello;

    @FXML
    private Label marchio;

    @FXML
    private Label motore;

    @FXML
    private Label dimensione;

    @FXML
    private Label isUsato;

    @FXML
    private Label labelUsato;

    @FXML
    private Label optional;

    @FXML
    private Label usatoModello;

    @FXML
    Label kmResult;
    @FXML
    Label kmLabel;
    @FXML   
    Label targa;
    @FXML
    Label targaLabel;
    @FXML
    ImageView image;
    private UserModel user = UserModel.getInstance();
    private RegistroModel registro = RegistroModel.getInstance();

    private Preventivo preventivo;

    public void setPreventivo(Preventivo preventivo) {

        this.preventivo = preventivo;
        if(!preventivo.getAcquisto().getImmagini().isEmpty()){
            image.setImage(preventivo.getAcquisto().getImmagini().get(0).getImage());
        }
        cliente.setText(preventivo.getCliente().getName() + " " + preventivo.getCliente().getSurname());
        sede.setText(preventivo.getSede().toString());
        data.setText(preventivo.getDataPreventivoAsString());
        consegna.setText(preventivo.getDataConsegnaAsString());
        if(preventivo.getStato()!= StatoPreventivo.RICHIESTO) {
            scadenza.setText(preventivo.getDataScadenzaAsString());
        }else{
            scadenza.setText("da definire");
        }
        stato.setText(preventivo.getStato().toString());
        costo.setText(preventivo.getCostoDettagliato());

        modello.setText(preventivo.getAcquisto().getModello());
        marchio.setText(preventivo.getAcquisto().getMarca().toString());
        motore.setText(preventivo.getMotoreScelto().getInfoMotore());
        dimensione.setText(preventivo.getAcquisto().getDimensione().toSimpleString());

        if(preventivo.getUsata() != null){
            isUsato.setText("Si");
            labelUsato.setText(preventivo.getUsata().getModello() + " " + preventivo.getUsata().getMarca());
            kmResult.setText(""+preventivo.getUsata().getKm()+"km");
            targa.setText(preventivo.getUsata().getTarga());
        }else{
            isUsato.setText("No");
            labelUsato.setText("");
            usatoModello.setText("");
            kmLabel.setText("");
            kmResult.setText("");
            targa.setText("");
            targaLabel.setText("");
        }
        String s="";
        if(preventivo.getOptionals().isEmpty()){
            optional.setText("No");
        }else{
        for(Optional opt : preventivo.getOptionals()){
                s += opt.toString() +"\n";
                optional.setText(s);
        }}
    }

    @FXML
    private void goBack() {
        try {
            TabPane tabPane = (TabPane) optional.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab tab= tabPane.getTabs().get(1); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventiviView.fxml"));
            BorderPane preventiviList;
            preventiviList = loader.load();
            PreventiviController controller = loader.getController();

            if(user.getCurrentUser() instanceof Cliente){
                controller.loadPrevs(registro.getPreventiviByCliente((Cliente) user.getCurrentUser()));
            }else{
                controller.loadPrevs(registro.getAllData());
            }
            tab.setContent(preventiviList); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    private void onPdf() {
        // Crea un nuovo documento PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Creazione di un contenuto per la pagina PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Use a different font that supports a wider range of characters
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);

                // Scrittura dei dati del preventivo nel PDF
                contentStream.showText("Cliente: " + preventivo.getCliente() );
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Sede: " + preventivo.getSede().getNome()+" "+preventivo.getSede().getIndirizzo());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Data: " + preventivo.getDataPreventivoAsString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Data Consegna: " + preventivo.getDataConsegnaAsString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Scadenza: " + preventivo.getDataScadenzaAsString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Stato: " + preventivo.getDataPreventivoAsString());
                //contentStream.newLine();
                //contentStream.showText("Costo: " + costo.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Modello: " +  preventivo.getAcquisto().getModello());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Marchio: " + preventivo.getAcquisto().getMarca().toString());
                contentStream.newLineAtOffset(0, -20);
                //contentStream.showText("Motore: " + motore.getText());
                //contentStream.newLine();
                //contentStream.showText("Dimensione: " + dimensione.getText());

                if (!isUsato.getText().equals("No")) {
    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Usato: " + labelUsato.getText());
    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Km: " + kmResult.getText());
    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Targa: " + targa.getText());
                }

                if (!optional.getText().equals("No")) {
    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Optionals:");
    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText(optional.getText());
                }

                contentStream.endText();
            }

            // Salva il documento PDF su disco
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
