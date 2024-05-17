package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

public class SinglePreventivoController {
    @FXML
    private Image icon;
    @FXML
    private Label modello;
    @FXML
    private Label cliente;
    @FXML
    private Label data;
    @FXML
    private Label consegna;
    @FXML
    private Label stato;
    @FXML
    private ImageView openIcon;
    UserModel utenti = UserModel.getInstance();
    RegistroModel registro = RegistroModel.getInstance();
    private Preventivo preventivo;

    public void setPreventivo(Preventivo preventivo) {
        this.preventivo = preventivo;
        modello.setText(preventivo.getAcquisto().getModello());
        data.setText(preventivo.getDataPreventivoAsString());
        consegna.setText(preventivo.getDataConsegnaAsString());
        stato.setText(preventivo.getStato().toString());
        setStatoColor(stato);

        if(utenti.getCurrentUser() instanceof Segretario){
            cliente.setText(preventivo.getCliente().getName());
        }
        String iconPath = "/img/icons/right-arrow.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        openIcon.setImage(icon);

    }
    //Funzione che permette la gestione del click sulla componente
    @FXML
    public void handleHBoxClicked(){
        //COMPLETARE
        //Controllo sul tipo di utente che ha cliccato
    }

    private void setStatoColor(Label stato){
        String statoAttuale = stato.getText();

        switch(statoAttuale){
            case "SCADUTO":
                stato.setTextFill(Color.RED);
                break;
            case "RITIRATO":
                stato.setTextFill(Color.BLACK);
                break;
            case "RICHIESTO":
                stato.setTextFill(Color.YELLOW);
            default:
                stato.setTextFill(Color.GREEN);
        }
    }
}
