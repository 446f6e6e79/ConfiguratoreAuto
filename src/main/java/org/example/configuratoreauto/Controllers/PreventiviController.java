package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Preventivi.*;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PreventiviController {

    private RegistroModel registro = RegistroModel.getInstance();
    private SediModel sedi = SediModel.getInstance();
    private UserModel utente = UserModel.getInstance();

    private StatoPreventivo actualStato;
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
        if (utente.getCurrentUser() instanceof Segretario) {
            setupForSegretario();
        } else if (utente.getCurrentUser() instanceof Cliente) {
            setupForCliente();
        } else if (utente.getCurrentUser() instanceof Impiegato) {
            setupForImpiegato();
        } else{
            Hyperlink registratiLink = new Hyperlink("Accedi per vedere i tuoi preventivi!");
            registratiLink.setOnAction(event -> openRegistratiView());
            mainView.getChildren().add(registratiLink);
        }
    }

    private void setupForSegretario() {
        choiceSede.setVisible(true);
        clienteField.setVisible(true);
        sedeLabel.setVisible(true);
        clienteLabel.setVisible(true);
        loadPrevs(registro.getAllData());
    }

    private void setupForCliente() {
        choiceSede.setVisible(false);
        clienteField.setVisible(false);
        sedeLabel.setVisible(false);
        clienteLabel.setVisible(false);
        loadPrevs(registro.getPreventiviByCliente((Cliente) utente.getCurrentUser()));
    }

    private void setupForImpiegato() {
        choiceSede.setVisible(true);
        clienteField.setVisible(true);
        sedeLabel.setVisible(true);
        clienteLabel.setVisible(true);
        titleMain.setText("Gestione Preventivi");

        initializaImpiegatoChoiceBox();
        actualStato = StatoPreventivo.FINALIZZATO; // Default state
        loadPrevs(registro.getPreventiviByStato(StatoPreventivo.FINALIZZATO));
    }

    private void initializaImpiegatoChoiceBox() {
        choiceStato.getItems().setAll(StatoPreventivo.FINALIZZATO, StatoPreventivo.PAGATO, StatoPreventivo.DISPONIBILE_AL_RITIRO);
        choiceStato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                actualStato = newValue;
                titleMain.setText("Gestione Preventivi");
                loadPrevs(registro.getPreventiviByStato(newValue));
            }
        });
        choiceStato.getSelectionModel().selectFirst();
    }

    @FXML
    public void resetFilter() {
        // Reset the values of ChoiceBoxes and TextField
        if(utente.getCurrentUser() instanceof Impiegato){
            choiceStato.getSelectionModel().select(0);
        }
        else{
            choiceStato.getSelectionModel().clearSelection();
        }
        choiceMarca.getSelectionModel().clearSelection();
        choiceSede.getSelectionModel().clearSelection();
        clienteField.clear();

        filterPreventivi();
        if(utente.getCurrentUser()instanceof Impiegato){
            loadPrevs(registro.getPreventiviByStato(StatoPreventivo.FINALIZZATO));
        }
        // Call filterPreventivi to refresh the list

    }

    private void initializeChoiceBoxes() {
        // Popola choiceStato con valori di StatoPreventivo
        choiceStato.getItems().setAll(StatoPreventivo.values());
        choiceStato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());

        // Popola choiceMarca con valori di Marca
        choiceMarca.getItems().setAll(Marca.values());
        choiceMarca.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());

        // Popola choiceSede con valori di Sede
        choiceSede.getItems().setAll(sedi.getAllData());
        choiceSede.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());

        // Listener per il campo di ricerca cliente
        clienteField.textProperty().addListener((observable, oldValue, newValue) -> filterPreventivi());
    }

    public void loadPrevs(ArrayList<Preventivo> registroArg) {
        // Resetto la lista presente in precedenza
        mainView.getChildren().clear();
        if(registro.currentPreventivo!=null){
            choiceStato.setValue(registro.currentPreventivo.getStato());
            registro.currentPreventivo=null;
        }
        if (utente.getCurrentUser() != null) {
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

    private void filterPreventivi() {
        StatoPreventivo selectedStato = choiceStato.getValue();
        Marca selectedMarca = choiceMarca.getValue();
        Sede selectedSede = choiceSede.getValue();
        String clienteQuery = clienteField.getText().toLowerCase();

        List<Preventivo> filteredList = registro.getAllData().stream()
                .filter(preventivo -> (selectedStato == null || preventivo.getStato() == selectedStato) &&
                        (selectedMarca == null || preventivo.getAcquisto().getMarca() == selectedMarca) &&
                        (selectedSede == null || preventivo.getSede().equals(selectedSede)) &&
                        (clienteQuery.isEmpty() || (preventivo.getCliente().getName().toLowerCase().contains(clienteQuery) ||
                                preventivo.getCliente().getSurname().toLowerCase().contains(clienteQuery))))
                .collect(Collectors.toList());

        loadPrevs(new ArrayList<>(filteredList));
    }
}
