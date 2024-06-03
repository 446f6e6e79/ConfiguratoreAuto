package org.example.configuratoreauto.Preventivi;


import org.example.configuratoreauto.AbstractModel;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Utenti.Cliente;
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

    /**
     * Metodi per gestire il preventivo attuale
     * @return
     */
    public Preventivo getCurrentPreventivo() {
        return currentPreventivo;
    }
    public void setCurrentPreventivo(Preventivo preventivo){
        currentPreventivo = preventivo;
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

    /**
    *   Override del metodo addData della classe AbstractModel:
    *       Il seguente metodo permette di gestire, durante la fase di caricamento del model
    *       l'aggiornamento dello stato del preventivo autonomamente
    * */
    @Override
    public boolean addData(Preventivo newPreventivo){
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
     * uguale nonostante i parametri di modifica siano diversi
     * @param preventivo
     */
    public void updateData(Preventivo preventivo){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(preventivo)) {
                data.set(i, preventivo);
                break;
            }
        }
    }

    /**
     * Metodi che permette di filtrare i preventivi in base a dati specifici
     * @return
     */
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
