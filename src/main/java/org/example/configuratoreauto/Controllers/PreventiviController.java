package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;
import org.example.configuratoreauto.Preventivi.Sede;

import java.io.IOException;
import java.util.ArrayList;

public class PreventiviController {

    RegistroModel registro = RegistroModel.getInstance();

    UserModel utente = UserModel.getInstance();

    @FXML
    VBox mainView;



    public void loadPrevs(ArrayList<Preventivo> registroArg) {
        //Resetto la lista presente in precedenza
        mainView.getChildren().clear();
        if(utente.getCurrentUser() != null){
            getPreventivi( registroArg );
        }else{
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
    private void getPreventivi(ArrayList<Preventivo> registroArg){
        if(registroArg.isEmpty()){
            Label none = new Label();
            none.setText("NESSUNA MACCHINA");
            mainView.getChildren().add(none);
            return;
        }
        for(Preventivo prev : registroArg){
            try {
                loadPreventivoElement(prev);
            }catch (IOException e){
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

    //Dovrebbe essere aperta la pagina di login
    private void handleLoginClick() {System.out.println("Login clicked");}

}
