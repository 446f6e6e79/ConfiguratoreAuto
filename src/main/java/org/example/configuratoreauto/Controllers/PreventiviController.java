package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;
import org.example.configuratoreauto.Preventivi.Sede;

import java.io.IOException;

public class PreventiviController {

    RegistroModel registro = RegistroModel.getInstance();

    UserModel utente = UserModel.getInstance();

    @FXML
    VBox mainView;


    public void initialize(){
        loadPrevs();
    }
    private void loadPrevs() {
        //Resetto la lista presente in precedenza
        mainView.getChildren().clear();
        if(utente.getCurrentUser() instanceof Cliente){
            getPreventiviForCliente();
        }else{
            Label loginLabel = new Label();
            loginLabel.setText("REGISTRATI");
            mainView.getChildren().add(loginLabel);
        }

    }
    @FXML
    void getPreventiviForCliente(){
        Cliente cliente = (Cliente) utente.getCurrentUser();
        if(registro.getPreventiviByCliente(cliente).isEmpty()){
            Label none = new Label();
            none.setText("NESSUNA MACCHINA");
            mainView.getChildren().add(none);
        }
        //Altrimenti, se l'utente è loggato, mostro la sua lista di preventivi
        else{
            for (Preventivo p : registro.getPreventiviByCliente(cliente)) {
                try {
                    loadPreventivoElement(p);
                }catch (IOException e){

                }

            }
        }
    }

    /*
    *   Genera la view dei Preventivi per l'utente IMPIEGATO:
    *       - vengono quindi mostrati i preventivi, per una determinata sede
    * */
    @FXML
    void getPreventiviforSede(Sede sede) {
        //COMPLETARE
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
