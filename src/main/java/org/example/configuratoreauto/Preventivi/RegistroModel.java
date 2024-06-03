package org.example.configuratoreauto.Preventivi;


import org.example.configuratoreauto.AbstractModel;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RegistroModel extends AbstractModel<Preventivo> {

    private static RegistroModel instance;

    /**
     *     Percorso al file contenete le informazioni riguardanti i preventivi
     */
    private static final String REGISTRO_PATH = "src/main/resources/data/registro.ser";

    /**
     *      Preventivo che è stato selezionato dal generico utente
     */
    private Preventivo currentPreventivo;

    private RegistroModel() {
        super();
    }


    //Gestione del preventivo corrente
    public Preventivo getCurrentPreventivo() {
        return currentPreventivo;
    }
    public void setCurrentPreventivo(Preventivo preventivo){
        currentPreventivo = preventivo;
    }

    /**
     * Restituisce l'unica istanza della classe registro
     * @return
     */
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

    /**
    *   Override del metodo addData della classe AbstractModel:
    *       Il seguente metodo permette di gestire, durante la fase di caricamento del model,
    *       l'aggiornamento automatico dello stato del preventivo
    * */
    @Override
    public boolean addData(Preventivo newPreventivo){
        if(newPreventivo == null){
            throw new IllegalArgumentException("Preventivo nullo inserito");
        }
        if(!data.contains(newPreventivo)){
            newPreventivo.updateStatoAutomatico();
            return data.add(newPreventivo);
        }
        return false;
    }

    /**
     * Metodo che si occupa di ricercare un preventivo specifico all'interno del registro
     * per poi aggiornalo col nuovo preventivo passato da argomento.
     * Ciò è possibile grazie all'implementazione equals del preventivo che identifica un preventivo
     * uguale nonostante i parametri differenti
     * @param preventivo
     */
    public void updateData(Preventivo preventivo){
        if(preventivo == null){
            throw new IllegalArgumentException("Preventivo nullo inserito");
        }
        int i = data.indexOf(preventivo);
        data.set(i, preventivo);
    }

    /**
     * Restituisce la lista di preventivi, relativi al cliente passato come parametro
     * @param cliente
     */
    public ArrayList<Preventivo> getPreventiviByCliente(Cliente cliente){
        if(cliente == null || !UserModel.getInstance().isCliente()){
            throw new IllegalArgumentException("Cliente nullo inserito");
        }
        return super.data.stream()
                .filter(t -> t.getCliente().equals(cliente))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Restituisce la lista di preventivi il quale stato combacia con quello passato come parametro
     */
    public ArrayList<Preventivo> getPreventiviByStato(StatoPreventivo s){
        return (filterPreventiviByStato(s, super.data));
    }

    /**
     * Metodi che permette di filtrare i preventivi in base a dati specifici
     * @return
     */
    public ArrayList<Preventivo> filterPreventiviByBrand(Marca brand, ArrayList<Preventivo> currentPreventivi){
        if(brand == null){
            throw new IllegalArgumentException("Marca nulla inserito");
        }
        return currentPreventivi.stream()
                .filter(t -> t.getAcquisto().getMarca() == brand)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> filterPreventiviBySede(Sede sede, ArrayList<Preventivo> currentPreventivi){
        if(sede == null){
            throw new IllegalArgumentException("Sede nulla inserito");
        }
        return currentPreventivi.stream()
                .filter(t -> t.getSede().equals(sede))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> filterPreventiviByStato(StatoPreventivo stato, ArrayList<Preventivo> currentPreventivi){
        if(stato == null){
            throw new IllegalArgumentException("Stato nullo inserito");
        }
        return currentPreventivi.stream()
                .filter(t -> t.getStato() == stato)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
