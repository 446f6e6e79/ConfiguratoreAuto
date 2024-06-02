package org.example.configuratoreauto.Macchine;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Pattern;

public class AutoUsata extends Auto implements Serializable {

    private final String targa;
    private final int km;

    public AutoUsata(Marca marca, String modello, String targa, int km) {
        super(marca, modello);
        //Segnalo il caso di targaVuota o non valida
        if (targa == null || !isValidTarga(targa)) {
            throw new IllegalArgumentException("Targa non valida");
        }
        this.targa=targa;

        if(km<0){
            throw new IllegalArgumentException("Km negativo");
        }
        this.km=km;
    }

    public String getTarga() {
        return targa;
    }
    
    public int getKm(){
        return km;
    }

    /**
     * Salva le immagini, contenute nell'arrayList immagini in memoria permanente
     */
    @Override
    public void addToLocalImages() {
        for(Immagine img:super.getImmagini()){
            img.addAutoUsata(this);
        }
    }

    /**
     *  Controlla la validità di una targa. Una targa è considerata valida se:
     *      1) La targa è composta correttamente
     *      2) La targa non è mai stata inserita all'interno di altri Preventivi
     * @param targa targa inserita da input
     * @return TRUE se la targa è VALIDA, FALSE altrimenti
     */
    public static boolean isValidTarga(String targa) {
        String regex = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$";
        return Pattern.matches(regex, targa) && !targaAlreadyPresent(targa);
    }

    /**
     * Controlla se la targa è già stata inserita in altri preventivi. In tal caso, lo segnala ritornando true
     * @param targa Targa che vogliamo verificare
     * @return TRUE se già presente, FALSE altrimenti
     */
    private static boolean targaAlreadyPresent(String targa) {
        File directory = new File( "/src/main/resources/img/usedCarImages");
        if (!directory.exists() || !directory.isDirectory()) {
            return false;
        }
        //Creo una lista di tutte le sotto-directory  di usedCarImages esistenti
        File[] files = directory.listFiles();
        if (files != null) {
            //Controllo, per ogni directory, se combacia con la targa inserita
            for (File file : files) {
                if (file.isDirectory() && file.getName().contains(targa)) {
                    return true;
                }
            }
        }
        return false;
    }
}
