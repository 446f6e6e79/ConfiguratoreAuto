package org.example.configuratoreauto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Preventivi.Preventivo;
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

    //Override del metodo STOP, viene eseguito alla terminazione del programma
    @Override
    public void stop() {
        closeModels();
    }
    public static void main(String[] args) {
        launch(args);
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

    private static void closeModels(){
        userModel.uploadData();
        System.out.println("Caricato userModel");
        registroModel.uploadData();
        System.out.println("Caricato registro model");


        catalogoModel.uploadData();
        System.out.println("Caricato catalogo model");
    }
}
