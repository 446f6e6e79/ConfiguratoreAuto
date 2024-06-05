package org.example.configuratoreauto;

import java.io.*;
import java.util.ArrayList;

/**
 *   Classe GENERICA per un MODELLO. Sono implementati i metodi principali, condivisi da ogni Modello:
 *       - laodData -> lettura delle informazioni dal file di testo
 *       - uploadData -> scritture delle informazioni aggiornate nel file di testo
 *       - addData -> aggiunge un singolo oggetto T1 ai dati
 *
 *   All'interno delle sottoclassi, sarà necessaria l'implementazione dei metodi:
 *       - getInstance -> metodo che implementa il pattern SIMPLETON, in modo da avere una singola istanza di ogni modello
 *       - getPathToData -> ritorna la stringa contenente il percorso al file
 * */
public abstract class AbstractModel<T1> {
    //ArrayList, contenente tutti i dati letti sul file
    protected ArrayList<T1> data;

    //Metodo che ritorna la stringa contenente il percorso al file
    protected abstract String getPathToData();

    /**
        Construttore per un mODEL generico:
        - inizializza una nuova ArrayList vuota
        - carica i dati, leggendoli dal file
     */
    protected AbstractModel(){
        data = new ArrayList<>();
        loadData();
    }

    /**
     *   loadData: legge i dati dal file, aggiungendoli all'arrayList.
     *      - I dati sono salvati sfruttando la SERIALIZZAZIONE
     *      - Una volta letto l'oggetto, viene castato al Tipo variabile T1
     * */
    private void loadData(){
        try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(getPathToData()))){
            //Oggetto temporaneo, rappresenta il singolo oggetto, letto dal file
            Object tempObj;
            while ((tempObj = objectInput.readObject()) != null) {
                this.addData((T1) tempObj);
            }
        }
        //Terminata la lettura dal file. Stampa la lista degli utenti a video, per debug
        catch (EOFException e){
            //printData();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *   Carica tutti i dati sul file, sfruttando la serializzazione
     * */
    protected void uploadData() {
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(getPathToData()))) {
            for(T1 elem: data){
                objectOutput.writeObject(elem);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Aggiunge alla lista l'oggetto passato come parametro, solamente nel caso
     *  in cui non fosse già presente
     */
    protected boolean addData(T1 newData){
        if(!data.contains(newData)){
            return data.add(newData);
        }
        return false;
    }

    /**
     * Restituisce tutti i dati contenuti all'interno del Modello
     */
    public final ArrayList<T1> getAllData(){
        return data;
    }
}
