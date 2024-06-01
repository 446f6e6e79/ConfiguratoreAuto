package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.Preventivi.Preventivo;

import java.io.Serializable;

public class Optional implements Serializable{

    private TipoOptional categoria;
    private String descrizione;
    private double costo;

    public Optional( TipoOptional categoria, String descrizione, double costo){
        this.categoria = categoria;
        this.descrizione = descrizione;
        this.costo = costo;
    }

    //Metodi get per i campi della classe OPTIONAL
    public double getCosto() {
        return costo;
    }
    public TipoOptional getCategoria() {
        return categoria;
    }
    public String getDescrizione() {
        return descrizione;
    }


    public String toString(){
        return descrizione +": "+ Preventivo.getPriceAsString(costo);
    }
}
