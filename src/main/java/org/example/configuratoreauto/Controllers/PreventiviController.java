package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;
import org.example.configuratoreauto.Preventivi.Sede;

public class PreventiviController {

    RegistroModel registro = RegistroModel.getInstance();

    @FXML
    VBox mainView;

    @FXML
    void getPreventiviForCliente() {
        mainView.getChildren().clear();
        Cliente cliente = (Cliente) UserModel.getInstance().getCurrentUser();
        if (cliente == null) {
            //Se l'utente è null, non è
            Label loginLabel = new Label("Accedi per vedere i tuoi preventivi");
            loginLabel.setId("clickableText");
            loginLabel.setOnMouseClicked(event -> handleLoginClick());

            mainView.getChildren().add(loginLabel);
            
        }
        //Altrimenti, se l'utente è loggato, mostro la sua lista di preventivi
        else{
            for (Preventivo p : registro.getPreventiviByCliente(cliente)) {
                HBox item = loadPreventivoElement(p);
                mainView.getChildren().add(item);
            }
        }
    }

    /*
    *   Genera la view dei Preventivi per l'utente IMPIEGATO:
    *       - vengono quindi mostrati i preventivi, per una determinata sede
    * */
    @FXML
    void getPreventiviForSegretario(Sede sede) {
        Segretario segretario = (Segretario) UserModel.getInstance().getCurrentUser();

        for (Preventivo p : registro.getPreventiviBySede( sede )) {
            HBox item =  loadPreventivoElement(p);
            mainView.getChildren().add(item);
        }
    }

    /*
    *   Genera il singolo elemento PREVENTIVO, contenente:
    *       - Data richiesta
    *       - Auto, per cui è stato richiesto il preventivo
    *       - Stato del preventivo
    * */
    private HBox loadPreventivoElement(Preventivo preventivo){
        HBox item = new HBox();
        Label date = new Label(preventivo.getDataPreventivoAsString());
        Label auto = new Label(preventivo.getAcquisto().getMarca() + " " + preventivo.getAcquisto().getModello());
        Label stato = new Label(preventivo.getStato().toString());
        item.getChildren().addAll(date, auto, stato);
        return item;
    }

    //Dovrebbe essere aperta la pagina di login
    private void handleLoginClick() {System.out.println("Login clicked");}

}
