package org.example.configuratoreauto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.configuratoreauto.Utenti.*;

public class Main extends Application {
    static UserModel userModel;
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("structure.fxml"));
        primaryStage.setTitle("JavaFX App with FXML and CSS");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        userModel = UserModel.getInstance();
    }

    public static void main(String[] args) {
        launch(args);

        //Once the program is closed, whole the data, saved locally, is saved to the files
        closeModels();
    }

    private static void setPage(String pageName) {
        String fxmlFile = pageName + ".fxml";
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
            Scene scene = new Scene(root);
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
    }
}
