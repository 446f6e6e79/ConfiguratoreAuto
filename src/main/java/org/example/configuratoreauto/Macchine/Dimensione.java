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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensione that = (Dimensione) o;
        return Double.compare(larghezza, that.larghezza) == 0 && Double.compare(altezza, that.altezza) == 0 && Double.compare(lunghezza, that.lunghezza) == 0 && Double.compare(peso, that.peso) == 0 && Double.compare(volumeBagagliaglio, that.volumeBagagliaglio) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(larghezza, altezza, lunghezza, peso, volumeBagagliaglio);
    }

    public String toString() {
        return "Dimensione:" +
                "\nlarghezza=" + larghezza +
                "\naltezza=" + altezza +
                "\nlunghezza=" + lunghezza +
                "\npeso=" + peso +
                "\nvolumeBagagliaglio=" + volumeBagagliaglio;
    }
}
