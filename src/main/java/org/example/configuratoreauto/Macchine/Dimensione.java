package org.example.configuratoreauto.Macchine;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe DIMENSIONE: descrive le principali informazioni per la dimensione
 * di un auto
 */
public class Dimensione implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private double larghezza;
    private double altezza;
    private double lunghezza;
    private double peso;
    private double volumeBagagliaglio;

    /**
     * Costruttore della classe Dimensione.
     * @param larghezza
     * @param altezza
     * @param lunghezza
     * @param peso
     * @param volumeBagagliaglio
     *
     * @throws IllegalArgumentException se uno dei campi non è valido
     */
    public Dimensione(double larghezza, double altezza, double lunghezza, double peso, double volumeBagagliaglio){
        if(altezza <= 0){
            throw new IllegalArgumentException("Altezza non valida");
        }
        this.altezza = altezza;

        if(lunghezza <= 0){
            throw new IllegalArgumentException("Lunghezza non valida");
        }
        this.lunghezza = lunghezza;

        if(larghezza <= 0){
            throw new IllegalArgumentException("Larghezza non valida");
        }
        this.larghezza = larghezza;

        if(peso <= 0){
            throw new IllegalArgumentException("Peso non valido");
        }
        this.peso = peso;

        //Permettiamo il caso volume = 0 (Auto senza bagagliaio)
        if(volumeBagagliaglio < 0){
            throw new IllegalArgumentException("Volume bagagliaio non valido");
        }
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
