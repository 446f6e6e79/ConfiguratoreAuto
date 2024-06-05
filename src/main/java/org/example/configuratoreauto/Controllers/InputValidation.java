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
    public static boolean checkValidStringTextField(TextField tf) {
        if(tf.getText().matches("[a-zA-Z0-9 ]+")){
            return true;
        }
        tf.clear();
        tf.requestFocus();
        return false;
    }

    /**
     * Verifica che il TextField, passato come parametro contenga double valido.
     * Consideriamo valido un double
     * @param tf TextField che vogliamo controllare
     * @return True se il texfield contiene una stringa valide, false altrimenti
     */
    public static boolean isValidDoubleTextField(TextField tf ){
        try {
            double value = Double.parseDouble(tf.getText());
            if(value < 0){
                tf.clear();
                tf.requestFocus();
            }
        } catch (NumberFormatException e) {
            tf.clear();
            tf.requestFocus();
            return false;
        }
        return true;
    }
}
