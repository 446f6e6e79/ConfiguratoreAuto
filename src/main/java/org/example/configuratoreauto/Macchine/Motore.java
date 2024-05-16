package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.Objects;

public class Motore implements Serializable {

    private Alimentazione alimentazione;
    private int potenzaKW;
    //Cilindrata, espressa in cm^3
    private int cilindrata;
    //Consumi, espressi in lt/100km
    private double consumi;

    public Motore(Alimentazione alimentazione, int potenzaKW, int cilindrata, double consumi){
        this.alimentazione = alimentazione;
        this.potenzaKW = potenzaKW;
        this.cilindrata = cilindrata;
        this.consumi = consumi;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Motore other &&
                this.alimentazione == other.alimentazione &&
                this.potenzaKW == other.potenzaKW &&
                this.consumi == other.consumi &&
                this.cilindrata == other.cilindrata;
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

    public int getCavalli(){
        return (int)( potenzaKW * 1.36);
    };
    public Alimentazione getAlimentazione() {
        return alimentazione;
    }

    public int getPotenzaKW() {
        return potenzaKW;
    }
}
