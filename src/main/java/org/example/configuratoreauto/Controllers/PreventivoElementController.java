package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

public class PreventivoElementController {
    @FXML
    private HBox mainElement;
    @FXML
    private Label modello;
    @FXML
    private Label cliente;
    @FXML
    private Label data;
    @FXML
    private Text consegnaDescription;
    @FXML
    private Label consegna;
    @FXML
    private Label stato;
    @FXML
    private ImageView openIcon;

    UserModel userModel = UserModel.getInstance();
    private Preventivo preventivo;

    public void setPreventivo(Preventivo preventivo) {
        this.preventivo = preventivo;
        modello.setText(preventivo.getAcquisto().getModello());
        data.setText(preventivo.getDataPreventivoAsString());

        //Se il preventivo è stato pagato, mostro la data di consegna
        if(preventivo.getStato() == StatoPreventivo.PAGATO ){
            consegna.setText(preventivo.getDataConsegnaAsString());
        }
        //Se lo stato è FINALIZZATO, mostro la data di scadenza
        else if(preventivo.getStato() == StatoPreventivo.FINALIZZATO){
            consegnaDescription.setText("Data scadenza:");
            consegna.setText(preventivo.getDataScadenzaAsString());
        }
        //Negli altri stati, la data di consegna non è disponibile
        else{
            consegnaDescription.setText("");
            consegna.setText("");
        }
        stato.setText(preventivo.getStato().toString());
        setStatoColor(stato, preventivo.getStato());

        //Se è in disponibile al ritiro, coloro di verde l'intero elento
        if(preventivo.getStato() == StatoPreventivo.DISPONIBILE_AL_RITIRO){
            mainElement.getStyleClass().clear();
            mainElement.getStyleClass().add("clickableElementGreen");
        }

        //Se SEGRETARIO o IMPIEGATO
        if(!userModel.isCliente()){
            cliente.setText(preventivo.getCliente().getName() + " " + preventivo.getCliente().getSurname());
        }
        String iconPath = "/img/icons/right-arrow.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        openIcon.setImage(icon);

    }
    //Funzione che permette la gestione del click sulla componente
    @FXML
    public void handlerClick(){
        try {
            TabPane tabPane = (TabPane) consegna.getScene().lookup("#mainPage"); // Ottieni il riferimento al TabPane
            Tab preventivoTab = tabPane.getTabs().get(1); // Ottieni il riferimento al tab "Catalogo"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/clienteView/preventivoDetails.fxml"));
            BorderPane preventivoNode = loader.load();
            PreventivoDetailsController controller = loader.getController();
            controller.setPreventivo(preventivo);
            preventivoTab.setContent(preventivoNode); // Imposta il nuovo contenuto del tab "Catalogo"
        }catch (IOException e){
                e.printStackTrace();
        }

    }

    private void setStatoColor(Label stato, StatoPreventivo statoPreventivo) {
        switch (statoPreventivo) {
            case SCADUTO:
                stato.setTextFill(Color.RED);
                break;
            case RITIRATO:
            case DISPONIBILE_AL_RITIRO:
                break;
            case RICHIESTO:
                stato.setTextFill(Color.web("#FFD700"));
                break;
            default:
                stato.setTextFill(Color.GREEN);
                break;
        }
    }



}
