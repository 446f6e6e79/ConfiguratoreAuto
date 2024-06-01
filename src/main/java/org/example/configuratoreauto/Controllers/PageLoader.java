package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PageLoader {

    public static void loadPage(String fxmlPath, String title, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(PageLoader.class.getResource(fxmlPath));
            Parent page = loader.load();
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateTab(){
        
    }

    public static void openDialog(String fxmlPath, String title, Parent parentContainer) {
        try {
            FXMLLoader loader = new FXMLLoader(PageLoader.class.getResource(fxmlPath));
            Parent dialogPane = loader.load();

            // Create a new Stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);

            // Set the scene with the loaded FXML
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            // Make the dialog modal and set its owner to the parent window
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentContainer.getScene().getWindow());

            // Show the dialog and wait until it is closed
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
