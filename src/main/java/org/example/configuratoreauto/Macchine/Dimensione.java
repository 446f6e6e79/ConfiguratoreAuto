package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.Objects;

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

    public Dimensione(){
        this.altezza = 0;
        this.lunghezza = 0;
        this.larghezza = 0;
        this.peso = 0;
        this.volumeBagagliaglio = 0;
    }

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

    @Override
    public boolean equals(Object o) {
        return o instanceof Dimensione other &&
                this.larghezza == other.larghezza &&
                this.altezza == other.altezza &&
                this.peso == other.peso &&
                this.volumeBagagliaglio == other.volumeBagagliaglio;

    }

    @Override
    public int hashCode() {
        return Objects.hash(larghezza, altezza, lunghezza, peso, volumeBagagliaglio);
    }

    public String toString() {
        return
                "\nLarghezza:" + larghezza +
                "m\nAltezza:" + altezza +
                "m\nLunghezza:" + lunghezza +
                "m\nPeso:" + peso +
                "kg\nVolume del Bagagliaglio:" + volumeBagagliaglio +"l";
    }
    public String toSimpleString(){
        return larghezza+"m x "+altezza+"m x "+lunghezza+"m";
    }
}
