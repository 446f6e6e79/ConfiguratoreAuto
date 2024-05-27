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
import java.util.Collection;
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
    private Label statoLabel;
    @FXML
    private ChoiceBox<StatoPreventivo> stateChoiceBox;
    @FXML
    private Label impiegatoStatoLabel;
    @FXML
    private void initialize() {
        impiegatoStatoLabel.setVisible(false);
        stateChoiceBox.setVisible(false);
        if (utente.getCurrentUser() instanceof Segretario) {
            choiceSede.setVisible(true);
            clienteField.setVisible(true);
            sedeLabel.setVisible(true);
            clienteLabel.setVisible(true);
            loadPrevs(registro.getAllData());
        } else if (utente.getCurrentUser() instanceof Cliente) {
            choiceSede.setVisible(false);
            clienteField.setVisible(false);
            sedeLabel.setVisible(false);
            clienteLabel.setVisible(false);
        } else if (utente.getCurrentUser() instanceof Impiegato) {
            choiceSede.setVisible(true);
            clienteField.setVisible(true);
            sedeLabel.setVisible(true);
            impiegatoStatoLabel.setVisible(true);
            stateChoiceBox.setVisible(true);
            clienteLabel.setVisible(true);
            choiceStato.setVisible(false);
            statoLabel.setVisible(false);
            titleMain.setText("Gestione Preventivi");

            initializeStateChoiceBox();
            actualStato = StatoPreventivo.FINALIZZATO; // Default state
            loadPrevs(registro.getPreventiviByStato(StatoPreventivo.FINALIZZATO));
        }
        initializeChoiceBoxes();
    }

    private void initializeStateChoiceBox() {
        stateChoiceBox.getItems().setAll(StatoPreventivo.FINALIZZATO, StatoPreventivo.PAGATO, StatoPreventivo.DISPONIBILE_AL_RITIRO);
        stateChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                actualStato = newValue;
                titleMain.setText("Gestione Preventivi - " + newValue);
                loadPrevs(registro.getPreventiviByStato(newValue));
            }
        });
        stateChoiceBox.getSelectionModel().selectFirst(); // Default to the first state
    }

    @FXML
    public void resetFilter() {
        // Reset the values of ChoiceBoxes and TextField
        choiceStato.getSelectionModel().clearSelection();
        choiceMarca.getSelectionModel().clearSelection();
        choiceSede.getSelectionModel().clearSelection();
        clienteField.clear();
        stateChoiceBox.getSelectionModel().select(0);
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
        if (utente.getCurrentUser() != null) {
            getPreventivi(registroArg);
        } else {
            Hyperlink registratiLink = new Hyperlink("Accedi per vedere i tuoi preventivi!");
            registratiLink.setOnAction(event -> openRegistratiView());
            mainView.getChildren().add(registratiLink);
        }
    }

    private void openRegistratiView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/loginPage.fxml"));
            VBox loginPane = loader.load();

            // Create a new Stage for the popup dialog
            Stage popupStage = new Stage();
            popupStage.setTitle("Registrati/Login");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(loginPane);
            popupStage.setScene(scene);

            // Make the dialog modal and set its owner to the current window
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(mainView.getScene().getWindow());

            // Show the dialog and wait until it is closed
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
