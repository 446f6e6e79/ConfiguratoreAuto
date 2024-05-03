package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

public class PreventiviController {

    RegistroModel registro = RegistroModel.getInstance();
    Cliente cliente = (Cliente) UserModel.getInstance().getCurrentUser();

    @FXML
    VBox list;

    @FXML
    void initialize(){
        for(Preventivo preventivo : registro.getPreventiviByCliente(cliente)){
            HBox item = new HBox();

            Label date = new Label(preventivo.getDataPreventivoAsString());
            Label auto = new Label(preventivo.getAcquisto().getMarca() + " " + preventivo.getAcquisto().getModello());
            Label stato = new Label(preventivo.getStato().toString());
            item.getChildren().add(date);
            item.getChildren().add(auto);
            item.getChildren().add(stato);
            list.getChildren().add(item);
        }

    }


}
