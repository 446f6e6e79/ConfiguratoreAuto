package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.StatoPreventivo;
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

        /*
        *   Resize dinamico della componente FlowPane, in modo da occupare l'intero schermo
        * */
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

    /**
     *  Aggiunge una componente AutoUsata per la sua valutazione
     * @param p Preventivo da valutare
     * @throws IOException
     */
    private void loadCarComponent(Preventivo p) throws IOException {
        // Carica la componente valutaUsato.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/impiegatoView/valutaUsatoElement.fxml"));
        VBox autoComponent = loader.load();

        // Configura il controller usatoComponent con i dati dell'auto
        AutoUsataElementController controller = loader.getController();
        controller.setElement(p);

        // Aggiungi l'usatoComponent al FlowPane
        list.getChildren().add(autoComponent);
    }




}