package org.example.configuratoreauto.Macchine;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

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
     Aggiungo l'immagine alla DIRECTORY interna al progetto:
        - Le cartelle sono gestite nel seguente modo:
            -carImages
                -idAuto_1
                    -colore_1
                    ...
                    -colore_n
                -idAuto_2
                 ...
            -usedCarImages
                -targaAuto1
                -targaAuto2
                ...
     */
    public void addToLocalImages() {
        if (this instanceof AutoNuova autoNuova) {

            //Aggiungo tutte le immagini relative alla macchina nella cartella temporanea
            for (Immagine img : immagini) {
                img.addAutoNuova(autoNuova);
            }

            //Sposto la directory tempImages carImages/auto.getId
            try {
                Immagine.cleanAndRenameDirectory(autoNuova);
            } catch (IOException e) {
                System.err.println("Error moving images to target directory: " + e.getMessage());
            }
        }

        //Sto aggiungendo immagini per un autoUsata
        else {
            for(Immagine img:immagini){
                img.addAutoUsata((AutoUsata) this);
            }
        }
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
