package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.AbstractModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogoModel extends AbstractModel<AutoNuova> {

    private static CatalogoModel instance;

    private AutoNuova selectedAuto;
    private AutoNuova tempAuto;
    private static Set<Integer> usedIds = new HashSet<>();
    private static Set<Marca> usedBrands = new HashSet<>();

    //Percorso al file contenete le informazioni riguardante il catalogo
    private static final String CATATLOGO_PATH = "src/main/resources/data/catalogo.ser";

    private CatalogoModel() {
        super();
    }

    public static CatalogoModel getInstance() {
        if (instance == null) {
            instance = new CatalogoModel();
        }
        return instance;
    }

    @Override
    protected String getPathToData() {
        return CATATLOGO_PATH;
    }

    /*
    *   Override del metodo addData. In questo modo, al caricamento del model verranno salvati
    *   in un set tutti gli ID già in uso
    * */
    @Override
    public boolean addData(AutoNuova newData){
        if(!data.contains(newData)){
            usedBrands.add(newData.getMarca());
            usedIds.add(newData.getId());
            return data.add(newData);
        }
        return false;
    }
    /*
    *   Passatogli come parametro una ArrayList di auto, restituisce un ArrayList contenente solamente le auto
    *   del brand passato come parametro
    * */
    public static ArrayList<AutoNuova> filterAutoByBrand(Marca brand, ArrayList<AutoNuova> data){
        return data.stream()
                .filter(t -> t.getMarca() == brand)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<AutoNuova> filterAutoByAlimentazione(Alimentazione alimentazione, ArrayList<AutoNuova> data) {
        return data.stream()
                .filter(auto -> auto.getMotoriDisponibili().stream().anyMatch(motore -> motore.getAlimentazione() == alimentazione))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<AutoNuova> filterAutoByPowerRange(int min, int max, ArrayList<AutoNuova> data) {
        return data.stream()
                .filter(t -> {
                    for (Motore motore : t.getMotoriDisponibili()) {
                        int potenzaKW = motore.getPotenzaKW();
                        return potenzaKW >= min && potenzaKW <= max;
                    }
                    return false;
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    public Set<Marca> getUsedBrands() {
        return usedBrands;
    }

    public static Set<Alimentazione> getUsedAlimentazione(ArrayList<AutoNuova> data) {
        HashSet<Alimentazione> usedAlimentazione = new HashSet<>();
        for(AutoNuova t : data){
            for(Motore m: t.getMotoriDisponibili()) {
                usedAlimentazione.add(m.getAlimentazione());
            }
        }
        return usedAlimentazione;
    }

    //Metodo per generare codiciUnivoci, assegnabili alle macchine
    public int getUniqueId(){
        int newId;
        //Continua a generare codici casuali, fino a che non ne trova uno nuovo > 0
        do {
            newId = UUID.randomUUID().hashCode();
        } while (usedIds.contains(newId) && newId < 0);
        return newId;
    }

    public AutoNuova getSelectedAuto() {
        return selectedAuto;
    }
    public void setSelectedAuto(AutoNuova auto){
        this.selectedAuto = auto;
    }

    /**
     *  Genero un'auto TEMPORANEA a cui posso applicare le modifiche.
     *  Una volta confermate, tali modifiche verranno passate al vero oggetto auto
     * @param autoToModify Auto che sto modificando
     */
    public void generateTempAuto(AutoNuova autoToModify){
        if(this.tempAuto == null){
            tempAuto = new AutoNuova();
        }
        //Se sto modificando una macchina, copio tutti i parametri di autoToModify in tempAuto
        if(autoToModify != null ){
            tempAuto = new AutoNuova(autoToModify);
        }
    }

    /**
     * Memorizza i dati di tempAuto in selectedAuto, confermando definitivamente le modifiche
     */
    public void saveTempData(){
        //Se stavo aggiungendo un nuovo modello, genero una nuova autoNuova
        if(selectedAuto == null){
            selectedAuto = new AutoNuova(getUniqueId());
        }
        //Salvo i dati all'interno di selectedAuto
        selectedAuto.setMarca(tempAuto.getMarca());
        selectedAuto.setModello(tempAuto.getModello());
        selectedAuto.setDescrizione(tempAuto.getDescrizione());
        selectedAuto.setDimensione(tempAuto.getDimensione());
        selectedAuto.setCostoBase(tempAuto.getCostoBase());
        selectedAuto.setScontoPerMese(tempAuto.getScontoPerMese());

        //Carico le immagini in memoria
        selectedAuto.setImmagini(tempAuto.getImmagini());

        //Salvo localmente tutte le immagini aggiunte
        selectedAuto.addToLocalImages();

        selectedAuto.setMotoriDisponibili(tempAuto.getMotoriDisponibili());
        selectedAuto.setOptionalDisponibili(tempAuto.getOptionalDisponibili());

        //Resetto tempAuto per le successive aggiunte
        tempAuto = null;
    }

    public void setTempAuto(AutoNuova tempAuto) {
        this.tempAuto = tempAuto;
    }

    public AutoNuova getTempAuto(){
        return tempAuto;
    }

}
