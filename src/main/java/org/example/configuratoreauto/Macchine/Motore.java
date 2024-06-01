package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.Objects;

public class Motore implements Serializable {

    private Alimentazione alimentazione;
    private int potenzaKW;
    private int cilindrata;
    private double consumi;
    private String nome;

    public Motore(String nome, Alimentazione alimentazione, int potenzaKW, int cilindrata, double consumi){
        this.nome = nome;
        this.alimentazione = alimentazione;
        this.potenzaKW = potenzaKW;
        this.cilindrata = cilindrata;
        this.consumi = consumi;
    }

    //Metodi get per i campi della classe Motore
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

    /**
     * Breve descrizione dell'oggetto MOTORE:
     *  - nome
     *  - alimentazione
     * @return
     */
    @Override
    public String toString() {
        return nome + " - " + alimentazione;
    }

    /**
     * Restituisce una descrizione delle info sul motore.
     * Ogni campo è seguito dalla propria unità di misura
     * @return
     */
    public String getInfoMotore() {
        String s = this.alimentazione+"\n"+
                "Potenza: "+this.potenzaKW +"Kw\n";

        if(cilindrata != 0){
            s += "Cilindrata: "+this.cilindrata+"cm³\n";
        }
        s+= "Consumi: "+this.consumi;
        s += (this.alimentazione == Alimentazione.ELETTRICA) ? " kWh/100km" : " l/100km";
        return s;
    }
}
