package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ValutazioneController implements Initializable {

    @FXML
    private BorderPane mainComponent;
    @FXML
    private FlowPane list;

    RegistroModel registro = RegistroModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        list.prefWidthProperty().bind(mainComponent.widthProperty());
        list.prefHeightProperty().bind(mainComponent.heightProperty());

        //Carico le auto
        load();

    }


    private void load() {
        //Resetto la lista presente in precedenza
        ArrayList<Preventivo> filteredList;
        filteredList = registro.getPreventiviByStato(StatoPreventivo.RICHIESTO);
        list.getChildren().clear();
        if(filteredList.isEmpty()){
            list.getChildren().add(new Text("Non è presente alcuna auto da valutare"));
        }


        for (Preventivo preventivo : filteredList) {
            try {
                loadCarComponent(preventivo);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *   Se l'utente è di tipo SEGRETARIO, introduce, in cima alla lista di auto
     *   un nuovo elemento cliccabile, con la possibilità di aggiungere un nuovo modello di auto
     * */


    private void loadCarComponent(Preventivo p) throws IOException {
        // Carica la componente autoElement.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/impiegatoView/autoUsataElement.fxml"));
        HBox autoComponent = loader.load();

        // Configura il controller dell'autoComponent con i dati dell'auto
        AutoUsataElementController controller = loader.getController();
        controller.setElement(p);

        // Aggiungi l'autoComponent al VBox
        list.getChildren().add(autoComponent);
    }




}