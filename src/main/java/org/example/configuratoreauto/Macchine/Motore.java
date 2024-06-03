package org.example.configuratoreauto.Macchine;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Motore implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Alimentazione alimentazione;
    private int potenzaKW;
    private int cilindrata;
    private double consumi;
    private String nome;

    /**
     * Costruttore per la classe Motore.
     * @throws IllegalArgumentException se uno dei campi ha un valore non valido
     */
    public Motore(String nome, Alimentazione alimentazione, int potenzaKW, int cilindrata, double consumi){
        if(nome == null || nome.isEmpty()){
            throw new IllegalArgumentException("Nome non valido");
        }
        this.nome = nome;

        this.alimentazione = alimentazione;
        if(potenzaKW <= 0){
            throw new IllegalArgumentException("Potenza non valida");
        }
        this.potenzaKW = potenzaKW;

        //Se è stato inserito un motore ad alimentazione ELETTRICA, verifico che la sua cilindrata sia nulla
        if (alimentazione == Alimentazione.ELETTRICA){
            if (cilindrata != 0) {
                throw new IllegalArgumentException("Non è possibile impostare una cilindrata per l'auto elettrica");
            }
        }
        else{
            if (cilindrata <= 0){
                throw new IllegalArgumentException("Cilindrata non valida");
            }
        }
        this.cilindrata = cilindrata;


        if(consumi <= 0){
            throw new IllegalArgumentException("Consumi non validi");
        }
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
