package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.Preventivi.Preventivo;

import java.io.Serializable;

public class Optional implements Serializable, Comparable<Optional> {

    private TipoOptional categoria;
    private String descrizione;
    private double costo;

    public Optional( TipoOptional categoria, String descrizione, double costo){
        this.categoria = categoria;
        this.descrizione = descrizione;
        this.costo = costo;
    }

    public double getCosto() {
        return costo;
    }

    public TipoOptional getCategoria() {
        return categoria;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public int hashCode(){
        return categoria.hashCode() ^ descrizione.hashCode();
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Optional opt &&
                this.categoria == opt.categoria &&
                this.descrizione.equals(opt.descrizione);
    }

    @Override
    public int compareTo(Optional other){
        int diff = this.categoria.compareTo(other.categoria);
        if(diff != 0){
            return diff;
        }
        return this.descrizione.compareTo(other.descrizione);
    }

    public String toString(){
        return descrizione +": "+ Preventivo.getPriceAsString(costo);
    }
}
