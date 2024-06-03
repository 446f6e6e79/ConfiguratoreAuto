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
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Segretario;
import org.example.configuratoreauto.Utenti.UserModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class PreventiviController {

    private RegistroModel registro = RegistroModel.getInstance();
    private SediModel sedi = SediModel.getInstance();
    private UserModel utente = UserModel.getInstance();

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
        resetFilter();
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
        loadPrevs(registro.getPreventiviByStato(StatoPreventivo.FINALIZZATO));
    }

    private void initializaImpiegatoChoiceBox() {
        choiceStato.getItems().setAll(StatoPreventivo.FINALIZZATO, StatoPreventivo.PAGATO, StatoPreventivo.DISPONIBILE_AL_RITIRO);
        choiceStato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
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

    /**
     * Metodo che si occupa di caricare la lista dei preventivi caricandone la singola componente FXML
     *
     * @param registroArg lista di preventivi di riferimento
     */
    public void loadPrevs(ArrayList<Preventivo> registroArg) {
        // Resetto la lista presente in precedenza
        mainView.getChildren().clear();
        if(registro.getCurrentPreventivo()!=null){
            choiceStato.setValue(registro.getCurrentPreventivo().getStato());
            registro.setCurrentPreventivo(null);
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

    /**
     * Filtra la lista di preventivi, a seconda dei filtri selezionati
     */
    private void filterPreventivi() {
        ArrayList<Preventivo> filteredList;
        if(utente.isCliente()){
            filteredList = registro.getPreventiviByCliente((Cliente) utente.getCurrentUser());
        }
        else{
            filteredList = registro.getAllData();
        }

        StatoPreventivo selectedStato = choiceStato.getValue();
        Marca selectedMarca = choiceMarca.getValue();
        Sede selectedSede = choiceSede.getValue();
        String clienteQuery = clienteField.getText().toLowerCase();

        if (selectedStato != null) {
            filteredList = registro.filterPreventiviByStato(selectedStato, filteredList);
        }
        if (selectedMarca != null) {
            filteredList = registro.filterPreventiviByBrand(selectedMarca,filteredList);
        }
        if (selectedSede != null) {
            filteredList = registro.filterPreventiviBySede(selectedSede, filteredList);
        }
        if (!clienteQuery.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(preventivo -> {
                        String fullName = preventivo.getCliente().getName().toLowerCase() + " " + preventivo.getCliente().getSurname().toLowerCase();
                        return fullName.contains(clienteQuery);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        loadPrevs(filteredList);
    }
}
