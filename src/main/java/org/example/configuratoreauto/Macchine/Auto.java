package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.ArrayList;



public abstract class Auto implements Serializable{
    private Marca marca;
    private String modello;
    private Dimensione dimensione;
    private ArrayList<Immagine> immagini;

    public Auto(Marca marca, String modello){
        this.marca = marca;
        this.modello = modello;
        this.immagini = new ArrayList<>();
    }

    protected void addImage(Immagine img){
        immagini.add(img);
    }

    public Marca getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }

    public String toString(){
        return this.modello;
    }

}
