package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.HashSet;

public abstract class Auto implements Serializable{
    private Marca marca;
    private String id;
    private Dimensione dimensione;
    private Motore motore;
    private HashSet<Immagine> immagini;
    public Auto(Marca marca, String id, Dimensione dimensione, Motore motore){
        this.marca = marca;
        this.id = id;
        this.dimensione = dimensione;
        this.motore = motore;
    }

    protected void addImage(Immagine img){
        immagini.add(img);
    }
    /*
    *   Due oggetti della classe auto sono considerabili equals se condividono stesso id
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof Auto other &&
                this.id.equals(other.id);
    }

    public Marca getMarca() {
        return marca;
    }

    public String getId() {
        return id;
    }

    public Dimensione getDimensione() {
        return dimensione;
    }

    public Motore getMotore() {
        return motore;
    }

    //Ridefiniamo il metodo hashCode, coerente con il metodo equals, in modo da poter inserire gli oggetti persona in un hashSet
    @Override
    public int hashCode(){
        return id.hashCode();
    }

    public String toString(){
        return this.id;
    }

}
