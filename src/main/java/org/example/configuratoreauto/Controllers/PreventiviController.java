package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Preventivi.*;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PreventiviController {

    RegistroModel registro = RegistroModel.getInstance();
    SediModel sedi = SediModel.getInstance();
    UserModel utente = UserModel.getInstance();

    @FXML
    VBox mainView;
    @FXML
    Label sedeLabel;
    @FXML
    Label clienteLabel;
    @FXML
    ComboBox<StatoPreventivo> choiceStato;
    @FXML
    ComboBox<Marca> choiceMarca;
    @FXML
    ComboBox<Sede> choiceSede;
    @FXML
    TextField clienteField;

    @FXML
    private void initialize() {
        if (utente.getCurrentUser() instanceof Cliente) {
            choiceSede.setVisible(false);
            clienteField.setVisible(false);
            sedeLabel.setVisible(false);
            clienteLabel.setVisible(false);
        } else {
            choiceSede.setVisible(true);
            clienteField.setVisible(true);
            sedeLabel.setVisible(true);
            clienteLabel.setVisible(true);
        }
        loadPrevs(registro.getAllData());
        initializeChoiceBoxes();
    }

    @FXML
    void resetFilter() {
        // Reset the values of ChoiceBoxes and TextField
        choiceStato.getSelectionModel().clearSelection();
        choiceMarca.getSelectionModel().clearSelection();
        choiceSede.getSelectionModel().clearSelection();
        clienteField.clear();

        // Call filterPreventivi to refresh the list
        filterPreventivi();
        loadPrevs(registro.getAllData());

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
        if (registroArg.isEmpty()) {
            Label none = new Label();
            none.setText("Non è presente nessuna macchina, controlla i filtri nel caso ti aspettassi qualcosa!!");
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

    /*
     *   Genera il singolo elemento PREVENTIVO, contenente:
     *       - Data richiesta
     *       - Auto, per cui è stato richiesto il preventivo
     *       - Stato del preventivo
     * */
    private void loadPreventivoElement(Preventivo a) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/configuratoreauto/preventivoComponent.fxml"));
        HBox preventivoComponent = loader.load();

        // Configura il controller dell'autoComponent con i dati dell'auto
        SinglePreventivoController controller = loader.getController();
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
