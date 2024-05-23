package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Auto implements Serializable{
    private Marca marca;
    private String modello;

    private ArrayList<Immagine> immagini = new ArrayList<>();

    public Auto(Marca marca, String modello){
        this.marca = marca;
        this.modello = modello;
        this.immagini = new ArrayList<>();
    }
    protected Auto(){}

    /*
    *   GET e SET per l'attrito MARCA
    * */
    public Marca getMarca() {
        return marca;
    }
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getModello() {
        return modello;
    }
    public void setModello(String modello) {
        this.modello = modello;
    }

    /**
     * Aggiunge un'immagine alla lista delle immagini di un'auto
     * */
    public void addImage(Immagine img){
        immagini.add(img);
    }

    /**
     *  Rimuove un immagine dalla lista di immagini dell'auto. L'immagine è inoltre eliminata dalla relativa directory
     * @param img immagine da rimuovere
     */
    public void removeImage(Immagine img){
        this.immagini.remove(img);
    }

    public ArrayList<Immagine> getImmagini(){
        return immagini;
    }

    public void setImmagini(ArrayList<Immagine> immagini) {
        this.immagini = immagini;
    }

    public String toString(){
        return this.modello;
    }

}
