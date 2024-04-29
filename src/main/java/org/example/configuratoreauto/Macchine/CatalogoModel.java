package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.AbstractModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CatalogoModel extends AbstractModel<AutoNuova> {

    private static CatalogoModel instance;

    //Percorso al file contenete le informazioni riguardanti gli utenti
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

    public ArrayList<AutoNuova> getAllAuto(){
        return super.data;
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
}
