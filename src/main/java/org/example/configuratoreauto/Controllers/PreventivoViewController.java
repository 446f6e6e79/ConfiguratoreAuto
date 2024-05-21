package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.example.configuratoreauto.Macchine.Optional;
import javafx.scene.image.ImageView;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;

public class PreventivoViewController {

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

    private UserModel user = UserModel.getInstance();
    private RegistroModel registro = RegistroModel.getInstance();

    public void setPreventivo(Preventivo preventivo) {
        cliente.setText(preventivo.getCliente().getName() + " " + preventivo.getCliente().getSurname());
        sede.setText(preventivo.getSede().toString());
        data.setText(preventivo.getDataPreventivoAsString());
        consegna.setText(preventivo.getDataConsegnaAsString());
        if(preventivo.getStato()!= StatoPreventivo.RICHIESTO) {
            scadenza.setText(preventivo.getDataScadenzaAsString());
        }else{
            scadenza.setText("...");
        }
        stato.setText(preventivo.getStato().toString());
        costo.setText(preventivo.getCostoDettagliato());

        modello.setText(preventivo.getAcquisto().getModello());
        marchio.setText(preventivo.getAcquisto().getMarca().toString());
        motore.setText(preventivo.getMotoreScelto().getInfoMotore());
        dimensione.setText(preventivo.getAcquisto().getDimensione().toSimpleString());

        if(preventivo.getUsata() != null){
            isUsato.setText("Si");
            usatoModello.setText(preventivo.getUsata().getModello() + " " + preventivo.getUsata().getMarca());
        }else{
            isUsato.setText("No");
            labelUsato.setText("");
            usatoModello.setText("");
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
}
