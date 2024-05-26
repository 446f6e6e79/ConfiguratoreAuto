package org.example.configuratoreauto.Macchine;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe Astratta auto. Rappresenta alcuni campi e metodi comuni alle classsi
 * AutoNuova e AutoUsata.
 */
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

    //GET e SET per l'attrito MARCA
    public Marca getMarca() {
        return marca;
    }
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    //GET e SET per l'attributo modello
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
     Aggiunge l'immagine alla DIRECTORY interna al progetto:
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
     *  Rimuove un immagine dalla lista di immagini dell'auto.
     *  L'immagine non viene però rimossa dalla memoria.
     */
    public void removeImage(Immagine img){
        this.immagini.remove(img);
    }

    /**
     * @return Lista di TUTTE le immagini disponibili per tale colore
     */
    public ArrayList<Immagine> getImmagini(){
        return immagini;
    }

    /**
     *
     * @param immagini Lista di immagini da settare all'oggetto Auto
     */
    public void setImmagini(ArrayList<Immagine> immagini) {
        this.immagini = immagini;
    }

    public String toString(){
        return this.modello;
    }
}
