package org.example.configuratoreauto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Utenti.*;

public class Main extends Application {
    static UserModel userModel;
    static RegistroModel registroModel;
    static CatalogoModel catalogoModel;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        registroModel = RegistroModel.getInstance();
        catalogoModel = CatalogoModel.getInstance();
        userModel = UserModel.getInstance();
        stage = primaryStage;
        stage.setMinWidth(960);
        stage.setMinHeight(540);
        setPage("loginPage");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        //Once the program is closed, whole the data, saved locally, is saved to the files
        closeModels();
    }

    public static void setPage(String pageName) {
        String fxmlFile = pageName + ".fxml";
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
        DA SPOSTARE IN UNA CLASSE MODEL GENERICA
    *   Metodo che aggiorna le istanze di tutti i modelli, prima della effettiva terminazione del programma
    * */
    private static void closeModels(){
        userModel.uploadData();
        System.out.println("Caricato userModel");
        registroModel.uploadData();
        System.out.println("Caricato registro model");
        catalogoModel.uploadData();
        System.out.println("Caricato catalogo model");
    }
}
