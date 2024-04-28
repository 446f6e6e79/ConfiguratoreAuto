package org.example.configuratoreauto.Macchine;

public class Dimensione {
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

    public String toString() {
        return "Dimensione:" +
                "\nlarghezza=" + larghezza +
                "\naltezza=" + altezza +
                "\nlunghezza=" + lunghezza +
                "\npeso=" + peso +
                "\nvolumeBagagliaglio=" + volumeBagagliaglio;
    }
}
