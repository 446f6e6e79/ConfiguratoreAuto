package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Preventivi.*;
import org.example.configuratoreauto.Utenti.UserModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PreventiviController {

    private RegistroModel registroModel = RegistroModel.getInstance();
    private SediModel sediModel = SediModel.getInstance();
    private UserModel userModel = UserModel.getInstance();

    @FXML
    private Label titleMain;
    @FXML
    private VBox mainView;
    @FXML
    private Label sedeLabel;
    @FXML
    private Label clienteLabel;
    @FXML
    private ComboBox<StatoPreventivo> choiceStato;
    @FXML
    private ComboBox<Marca> choiceMarca;
    @FXML
    private ComboBox<Sede> choiceSede;
    @FXML
    private TextField clienteField;

    @FXML
    private void initialize() {
        initializeChoiceBoxes();
        if (userModel.isSegretario()) {
            setupForSegretario();
        } else if (userModel.isCliente()) {
            setupForCliente();
        } else if (userModel.isImpiegato()) {
            setupForImpiegato();
        } else{
            Hyperlink registratiLink = new Hyperlink("Accedi per vedere i tuoi preventivi!");
            registratiLink.setOnAction(event -> openRegistratiView());
            mainView.getChildren().add(registratiLink);
        }
        filterPreventivi();
    }

    private void setupForSegretario() {
        choiceSede.setVisible(true);
        clienteField.setVisible(true);
        sedeLabel.setVisible(true);
        clienteLabel.setVisible(true);
    }

    private void setupForCliente() {
        choiceSede.setVisible(false);
        clienteField.setVisible(false);
        sedeLabel.setVisible(false);
        clienteLabel.setVisible(false);
    }

    private void setupForImpiegato() {
        choiceSede.setVisible(true);
        clienteField.setVisible(true);
        sedeLabel.setVisible(true);
        clienteLabel.setVisible(true);
        titleMain.setText("Gestione Preventivi");
        initializaImpiegatoChoiceBox();
    }

    private void initializaImpiegatoChoiceBox() {
        choiceStato.getItems().setAll(StatoPreventivo.FINALIZZATO, StatoPreventivo.PAGATO, StatoPreventivo.DISPONIBILE_AL_RITIRO);
        choiceStato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                titleMain.setText("Gestione Preventivi");
                loadPrevs(registroModel.filterPreventivi(newValue, null, null, null));
            }
        });
        choiceStato.getSelectionModel().selectFirst();
    }

    @FXML
    public void resetFilter() {
        //Resetto i valori per le choiceBox dei filtri
        if(userModel.isImpiegato()){
            choiceStato.getSelectionModel().select(0);
        }
        else{
            choiceStato.getSelectionModel().clearSelection();
        }
        choiceMarca.getSelectionModel().clearSelection();
        choiceSede.getSelectionModel().clearSelection();
        clienteField.clear();
    }

    private void initializeChoiceBoxes() {
        // Popola choiceStato con valori di StatoPreventivo
        choiceStato.getItems().setAll(StatoPreventivo.values());
        choiceStato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());

        // Popola choiceMarca con valori di Marca
        choiceMarca.getItems().setAll(Marca.values());
        choiceMarca.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());

        // Popola choiceSede con valori di Sede
        choiceSede.getItems().setAll(sediModel.getAllData());
        choiceSede.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());

        // Listener per il campo di ricerca cliente
        clienteField.textProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());
    }

    /**
     * Metodo che si occupa di caricare la lista dei preventivi caricandone la singola componente FXML
     * @param registroArg lista di preventivi di riferimento
     */
    public void loadPrevs(ArrayList<Preventivo> registroArg) {
        // Resetto la lista presente in precedenza
        mainView.getChildren().clear();
        if(registroModel.getCurrentPreventivo()!=null){
            //choiceStato.setValue(registroModel.getCurrentPreventivo().getStato());
            registroModel.setCurrentPreventivo(null);
        }
        if (userModel.getCurrentUser() != null) {
            getPreventivi(registroArg);
        } else {
            Hyperlink registratiLink = new Hyperlink("Accedi per vedere i tuoi preventivi!");
            registratiLink.setOnAction(event -> openRegistratiView());
            mainView.getChildren().add(registratiLink);
        }
    }

    private void openRegistratiView() {
        PageLoader.openDialog("/org/example/configuratoreauto/loginPage.fxml", "Registrati/Login", mainView);
    }

    private void getPreventivi(ArrayList<Preventivo> registroArg) {
        Collections.sort(registroArg);
        if (registroArg.isEmpty()) {
            Label none = new Label();
            none.setText("Non è presente alcun preventivo che rispetti i filtri selezionati!");
            none.setFont(new Font(30));
            none.setPadding(new Insets(300,0,0,0));
            mainView.getChildren().add(none);
            return;
        }
        for (Preventivo prev : registroArg) {
            try {
                loadPreventivoElement(prev);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *   Genera il singolo elemento PREVENTIVO, contenente:
     *       - Data richiesta
     *       - Auto, per cui è stato richiesto il preventivo
     *       - Stato del preventivo
     * */
    private void loadPreventivoElement(Preventivo a) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventivoElement.fxml"));
        HBox preventivoComponent = loader.load();

        // Configura il controller dell'autoComponent con i dati dell'auto
        PreventivoElementController controller = loader.getController();
        controller.setPreventivo(a);

        // Aggiungi l'autoComponent al VBox
        mainView.getChildren().add(preventivoComponent);
    }

    /**
     * Filtra la lista di preventivi, a seconda dei filtri selezionati
     */
    private void filterPreventivi() {
        StatoPreventivo selectedStato = choiceStato.getValue();
        Marca selectedMarca = choiceMarca.getValue();
        Sede selectedSede = choiceSede.getValue();
        String clienteQuery = clienteField.getText().toLowerCase();
        ArrayList<Preventivo> filteredList = registroModel.filterPreventivi(selectedStato, selectedMarca, selectedSede, clienteQuery);

        //Carico i preventivi
        loadPrevs(filteredList);

    }
}
