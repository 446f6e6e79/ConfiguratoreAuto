package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.ArrayList;



public abstract class Auto implements Serializable{
    private Marca marca;
    private String modello;
    private Dimensione dimensione;
    private Motore motore;
    private ArrayList<Immagine> immagini;
    public Auto(Marca marca, String modello, Dimensione dimensione, Motore motore){
        this.marca = marca;
        this.modello = modello;
        this.dimensione = dimensione;
        this.motore = motore;
        this.immagini = new ArrayList<>();
    }

    protected void addImage(Immagine img){
        immagini.add(img);
    }
    /*
    *   Due oggetti della classe auto sono considerabili equals se condividono stesso id
    * */

    public boolean equals(Object o){
        return o instanceof Auto other &&
                this.marca.equals(other.marca) &&
                this.modello.equals(other.modello) &&
                this.dimensione.equals(other.dimensione) &&
                this.motore.equals(other.motore);
    }

    public Marca getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
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
        return motore.hashCode()+ dimensione.hashCode() + marca.hashCode() + modello.hashCode();
    }

    public String toString(){
        return this.modello;
    }

}
