package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe DIMENSIONE: descrive le principali informazioni per la dimensione
 * di un auto
 */
public class Dimensione implements Serializable {
    private double larghezza;
    private double altezza;
    private double lunghezza;
    private double peso;
    private double volumeBagagliaglio;

    public Dimensione(double larghezza, double altezza, double lunghezza, double peso, double volumeBagagliaglio){
        this.altezza = altezza;
        this.lunghezza = lunghezza;
        this.larghezza = larghezza;
        this.peso = peso;
        this.volumeBagagliaglio = volumeBagagliaglio;
    }

    //Metodi GET per tutti i campi della classe Dimensione
    public double getAltezza() {
        return altezza;
    }
    public double getLunghezza() {
        return lunghezza;
    }
    public double getLarghezza() {
        return larghezza;
    }
    public double getPeso() {
        return peso;
    }
    public double getVolumeBagagliaglio() {
        return volumeBagagliaglio;
    }

    /**
     *
     * @return Stringa formattata come:
     * <p>  - Larghezza val m
     * <p>  - Altezza val m
     * <p>  - Lunghezza val m
     * <p>  - Peso val kg
     * <p>  - Volume del bagagliaio  val l
     */
    public String toString() {
        return
                "\nLarghezza:" + larghezza +
                "m\nAltezza:" + altezza +
                "m\nLunghezza:" + lunghezza +
                "m\nPeso:" + peso +
                "kg\nVolume del Bagagliaglio:" + volumeBagagliaglio +"l";
    }

    /**
     * @return Stringa che riassume le dimensioni della macchina:
     *  larghezza x altezza x lunghezza
     */
    public String toSimpleString(){
        return larghezza+"m x "+altezza+"m x "+lunghezza+"m";
    }
}
