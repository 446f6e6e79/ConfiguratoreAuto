package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.UserModel;
import org.example.configuratoreauto.Preventivi.Sede;

public class PreventiviController {

    RegistroModel registro = RegistroModel.getInstance();

    @FXML
    VBox mainView;

    @FXML
    void getPreventiviForCliente() {
        Cliente cliente = (Cliente) UserModel.getInstance().getCurrentUser();
        if (cliente == null) {
            // Current user is null, display clickable text
            Label loginLabel = new Label("Accedi per vedere i tuoi preventivi");
            loginLabel.setId("clickableText");
            loginLabel.setOnMouseClicked(event -> handleLoginClick());
            mainView.getChildren().add(loginLabel);
            
        } else {
            // Current user is not null, display preventivi
            for (Preventivo preventivo : registro.getPreventiviByCliente(cliente)) {
                HBox item = new HBox();

                Label date = new Label(preventivo.getDataPreventivoAsString());
                Label auto = new Label(preventivo.getAcquisto().getMarca() + " " + preventivo.getAcquisto().getModello());
                Label stato = new Label(preventivo.getStato().toString());
                item.getChildren().addAll(date, auto, stato);
                mainView.getChildren().add(item);
            }
        }
    }

    @FXML
    void getPreventiviForImpiegato(Sede sede) {
        Impiegato impiegato = (Impiegato) UserModel.getInstance().getCurrentUser();

        for (Preventivo preventivo : registro.getPreventiviBySede( sede )) {
            HBox item = new HBox();

            Label date = new Label(preventivo.getDataPreventivoAsString() +" ");
            Label auto = new Label(preventivo.getAcquisto().getMarca() + " " + preventivo.getAcquisto().getModello()+ " ");
            Label stato = new Label(preventivo.getStato().toString()+" ");
            Label cliente = new Label((preventivo.getCliente().getName()));

            item.getChildren().addAll(date, auto, stato, cliente);
            mainView.getChildren().add(item);
        }
    }

    private void handleLoginClick() {System.out.println("Login clicked");}

}
