package org.example.configuratoreauto.Macchine;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Motore motore = (Motore) o;
        return potenzaKW == motore.potenzaKW && cilindrata == motore.cilindrata && Double.compare(consumi, motore.consumi) == 0 && alimentazione == motore.alimentazione;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alimentazione, potenzaKW, cilindrata, consumi);
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
