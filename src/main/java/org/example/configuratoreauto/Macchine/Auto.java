package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.ArrayList;



public abstract class Auto implements Serializable{
    private Marca marca;
    private String modello;

    private ArrayList<Immagine> immagini;

    public Auto(Marca marca, String modello){
        this.marca = marca;
        this.modello = modello;
        this.immagini = new ArrayList<>();
    }

    public void addImage(Immagine img){
        immagini.add(img);
    }

    /**
     *  Rimuove un immagine dalla lista di immagini dell'auto. L'immagine è inoltre eliminata dalla relativa directory
     * @param img immagine da rimuovere
     */
    public void removeImage(Immagine img){
        if(this.immagini.remove(img)){
            //Elimino il file dalla directory
            img.delete();
        }
    }

    public ArrayList<Immagine> getImmagini(){
        return immagini;
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
