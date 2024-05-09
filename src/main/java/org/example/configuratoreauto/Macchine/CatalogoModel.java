package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.AbstractModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;      //Permette di generare randomicamente codici univoci

public class CatalogoModel extends AbstractModel<AutoNuova> {

    private static CatalogoModel instance;

    private static Set<Integer> usedIds = new HashSet<>();

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
    protected boolean addData(AutoNuova newData){
        if(!data.contains(newData)){
            usedIds.add(newData.getId());
            return data.add(newData);
        }
        return false;
    }
    public ArrayList<AutoNuova> getAutoByBrand(Marca brand){
        return super.data.stream()
                .filter(t -> t.getMarca() == brand)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<AutoNuova> getAutoByBrand(Alimentazione alimentazione){
        return super.data.stream()
                .filter(t -> t.getMotore().getAlimentazione() == alimentazione)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<AutoNuova> getAutoByPowerRange(int min, int max){
        return super.data.stream()
                .filter(t -> t.getMotore().getPotenzaKW() >= min && t.getMotore().getPotenzaKW() <= max)
                .collect(Collectors.toCollection(ArrayList::new));
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
}
