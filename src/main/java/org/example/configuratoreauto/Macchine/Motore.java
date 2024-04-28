package org.example.configuratoreauto.Macchine;

public class Motore {

    private Alimentazione alimentazione;
    private int potenzaKW;
    private int cilindrata;
    private double consumi;

    public Motore(Alimentazione alimentazione, int potenzaKW, int cilindrata, double consumi){
        this.alimentazione = alimentazione;
        this.potenzaKW = potenzaKW;
        this.cilindrata = cilindrata;
        this.consumi = consumi;
    }

    public double getConsumi() {
        return consumi;
    }

    public int getCilindrata() {
        return cilindrata;
    }

    public Alimentazione getAlimentazione() {
        return alimentazione;
    }

    public int getPotenzaKW() {
        return potenzaKW;
    }
}
