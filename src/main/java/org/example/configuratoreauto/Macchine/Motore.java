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
    private String nome;
    public Motore(String nome, Alimentazione alimentazione, int potenzaKW, int cilindrata, double consumi){
        this.nome = nome;
        this.alimentazione = alimentazione;
        this.potenzaKW = potenzaKW;
        this.cilindrata = cilindrata;
        this.consumi = consumi;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Motore other &&
                this.alimentazione == other.alimentazione &&
                this.nome.equals(other.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alimentazione, nome);
    }

    public double getConsumi() {
        return consumi;
    }

    public int getCilindrata() {
        return cilindrata;
    }

    public String getNome(){return nome;}
    public int getCavalli(){
        return (int)( potenzaKW * 1.36);
    };
    public Alimentazione getAlimentazione() {
        return alimentazione;
    }

    public int getPotenzaKW() {
        return potenzaKW;
    }

    @Override
    public String toString() {
        return nome + " - " + alimentazione;
    }
    public String getInfoMotore() {
        String s = "Alimentazione: "+this.alimentazione+"\n"+
                "Potenza KW: "+this.potenzaKW +"\n";

        if(cilindrata != 0){
            s += "Cilindrata: "+this.cilindrata+"\n";
        }
        s+= "Consumi: "+this.consumi+"\n";
        return s;
    }


}
