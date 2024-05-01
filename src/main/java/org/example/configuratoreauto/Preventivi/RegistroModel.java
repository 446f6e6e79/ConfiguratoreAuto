package org.example.configuratoreauto.Preventivi;


import org.example.configuratoreauto.AbstractModel;
import org.example.configuratoreauto.Macchine.Alimentazione;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RegistroModel extends AbstractModel<Preventivo> {

    private static RegistroModel instance;

    //Percorso al file contenete le informazioni riguardanti i preventivi
    private static final String REGISTRO_PATH = "src/main/resources/data/registro.ser";

    private RegistroModel() {
        super();
    }

    public static RegistroModel getInstance() {
        if (instance == null) {
            instance = new RegistroModel();
        }
        return instance;
    }

    @Override
    protected String getPathToData() {
        return REGISTRO_PATH;
    }

    /*
    *   Override del metodo addData della classe AbstractModel:
    *       Il seguente metodo permette di gestire, durante la fase di caricamento del model
    *       l'aggiornamento dello stato del preventivo autonomamente
    * */

    @Override
    public boolean addData(Preventivo newPreventivo){
        if(!data.contains(newPreventivo)){
            if(newPreventivo.isScaduto()){
                newPreventivo.changeStato(StatoPreventivo.SCADUTO);
            }
            if(newPreventivo.isDisponibileAlRitiro()){
                newPreventivo.changeStato(StatoPreventivo.DISPONIBILE_AL_RITIRO);
            }
            return data.add(newPreventivo);
        }
        return false;
    }

    public ArrayList<Preventivo> getPreventiviByBrand(Marca brand){
        return super.data.stream()
                .filter(t -> t.getAcquisto().getMarca() == brand)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> getPreventiviByCliente(Cliente cliente){
        return super.data.stream()
                .filter(t -> t.getCliente().equals(cliente))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> getPreventiviBySede(Sede sede){
        return super.data.stream()
                .filter(t -> t.getSede().equals(sede))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> getPreventiviByStato(StatoPreventivo stato){
        return super.data.stream()
                .filter(t -> t.getStato() == stato)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
