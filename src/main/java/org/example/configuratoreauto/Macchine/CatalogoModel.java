package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.AbstractModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;      //Permette di generare randomicamente codici univoci

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
        //Continua a generare codici casuali, fino a che non ne trova uno nuovo
        do {
            newId = UUID.randomUUID().hashCode();
        } while (usedIds.contains(newId));
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
            tempAuto.setMarca(autoToModify.getMarca());
            tempAuto.setModello(autoToModify.getModello());
            tempAuto.setDescrizione(autoToModify.getDescrizione());
            tempAuto.setDimensione(autoToModify.getDimensione());
            tempAuto.setCostoBase(autoToModify.getCostoBase());
            tempAuto.setScontoPerMese(autoToModify.getScontoPerMese());
            tempAuto.setImmagini(autoToModify.getImmagini());
            tempAuto.setMotoriDisponibili(autoToModify.getMotoriDisponibili());
            tempAuto.setOptionalDisponibili(autoToModify.getOptionalDisponibili());
        }
    }

    public AutoNuova getTempAuto(){
        return tempAuto;
    }

}
