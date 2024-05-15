package org.example.configuratoreauto.Macchine;

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
        return categoria.hashCode() ^ descrizione.hashCode() ^ (int)costo;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Optional opt &&
                this.categoria == opt.categoria &&
                this.costo == opt.costo &&
                this.descrizione.equals(opt.descrizione);
    }

    @Override
    public int compareTo(Optional other){
        return this.categoria.compareTo(other.categoria);
    }

    public String toString(){
        return descrizione +": "+costo;
    }
}
