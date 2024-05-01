package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.AbstractModel;

public class SediModel extends AbstractModel<Sede> {
    private static SediModel instance;

    //Percorso al file contenete le informazioni riguardanti i preventivi
    private static final String SEDI_PATH = "src/main/resources/data/sedi.ser";

    private SediModel() {
        super();
    }

    public static SediModel getInstance() {
        if (instance == null) {
            instance = new SediModel();
        }
        return instance;
    }

    @Override
    protected String getPathToData() {
        return SEDI_PATH;
    }

}
