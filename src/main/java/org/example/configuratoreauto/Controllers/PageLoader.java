package org.example.configuratoreauto.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PageLoader {

    /**Metodo che ha lo scopo di caricare un fxml sovrascrivendo la pagina attuale
     *
     * @param fxmlPath path della view da caricare
     * @param title titolo della scena
     * @param stage riferimento della pagina da modificare
     */
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

    /**Aggiorna il contenuto di un tab di una data TabPane, utile per aggiornare catalogo e preventivi
     *
     * @param tabPane TabPane di riferimento
     * @param tabIndex Indice del tab che si desidera modificare
     * @param fxmlPath  View da caricare
     */
    public static void updateTabContent(TabPane tabPane, int tabIndex, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(PageLoader.class.getResource(fxmlPath));
            Parent content = loader.load();
            Tab tab = tabPane.getTabs().get(tabIndex);
            tab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un pop-up che segnala un qualsiasi errore
     * @param title
     * @param message
     */
    public static void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Genere una schermata popup con una desiderata view caricata al suo interno
     * @param fxmlPath view da caricare
     * @param title titolo della pagina
     * @param parentContainer riferimento della schermata che lo deve generare
     */
    public static void openDialog(String fxmlPath, String title, Parent parentContainer) {
        try {
            FXMLLoader loader = new FXMLLoader(PageLoader.class.getResource(fxmlPath));
            Parent dialogPane = loader.load();
            // Crea un nuovo stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            //Imposta la view sulla scena
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            //Imposta la window che dovrebbe generare la dialog come owner del popup
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentContainer.getScene().getWindow());
            //Mosta la dialog e aspetta la sua chiusura
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
