package org.example.configuratoreauto.Controllers;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class InputValidation {

    /**
     *  Funzione che blocca input non validi, nei TextField che richiedono l'inserimento di campi integer
     * @param event evento KeyEvent, rappresenta la pressione di un tasto
     * @param tf TextField, campo di input
     */
    public static void checkValidInt(KeyEvent event, TextField tf) {
        // Leggo il carattere che ha generato l'evento
        String character = event.getCharacter();

        /*
            Blocco l'input di un qualsiasi tasto, diverso da:
                - numero da 0 - 9
         */
        if (!character.matches("[0-9]")) {
            event.consume();
            //Aggiungo un "consiglio"
        }
    }

    /**
     *  Funzione che blocca input non validi, nei TextField che richiedono l'inserimento di campi double
     * @param event evento KeyEvent, rappresenta la pressione di un tasto
     * @param tf TextField, campo di input
     */
    public static void checkValidDouble(KeyEvent event, TextField tf){
        //Leggo il carattere che ha generato l'evento
        String character = event.getCharacter();

        /*
            Blocco l'input di un qualsiasi tasto, diverso da:
                - numero da 0 - 9
                - punto
         */
        if (!character.matches("[0-9.]")) {
            event.consume();
        }
        //Blocca la presenza di più punti
        if (character.equals(".") && tf.getText().contains(".")) {
            event.consume();
        }
    }

    /**
     * Verifica che il TextField, passato come parametro contenga una stringa valida.
     * Una stringa è considerata valida se contiene:
     *  - catatteri minuscoli o maiuscoli, dell'alfabeto
     *  - numeri
     * Sono di conseguenza vietati
     * @param tf TextField che vogliamo controllare
     * @return True se il texfield contiene una stringa valide, false altrimenti
     */
    public static boolean isValidStringTextField(TextField tf) {
        cleanTextFieldStyle(tf);
        if(tf.getText().matches("[a-zA-Z0-9 ]+")){
            return true;
        }
        tf.clear();
        tf.requestFocus();
        return false;

    }

    /**
     * Verifica che il TextField, passato come parametro contenga double valido.
     * @param tfList lista di TextField che vogliamo controllare
     * @param canBeZero indica se il campo textField può o meno valere 0
     * @return True se il texfield contiene una stringa valide, false altrimenti
     */
    public static boolean isValidDoubleTextField(boolean canBeZero, TextField... tfList){
        boolean result = true;
        cleanTextFieldStyle(tfList);
        for(TextField tf: tfList) {
            try {
                double value = Double.parseDouble(tf.getText());
                if (value < 0 || (value == 0 && !canBeZero)) {
                    tf.clear();
                    tf.setStyle("-fx-border-color: red;");
                    tf.requestFocus();
                    result = false;
                }
            } catch (NumberFormatException e) {
                tf.clear();
                tf.setStyle("-fx-border-color: red;");
                tf.requestFocus();
                result = false;
            }
        }
        return result;
    }

    /**
     * Pulisce il colore che è stato assegnato ai campi non validi
     * @param tfList lista di textField
     */
    private static void cleanTextFieldStyle(TextField... tfList){
        for(TextField tf: tfList){
            tf.setStyle("");
        }
    }
}
