package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.AbstractModel;

import java.io.IOException;
import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogoModel extends AbstractModel<AutoNuova> {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Istanza univoca della classe */
    private static CatalogoModel instance;

    /** Rappresenta l'auto selezionata */
    private AutoNuova selectedAuto;
    private AutoNuova tempAuto;

    private static Set<Integer> usedIds = new HashSet<>();
    private static Set<Marca> usedBrands = new HashSet<>();

    /**Percorso al file contenete le informazioni riguardante il catalogo*/
    private static final String CATATLOGO_PATH = "src/main/resources/data/catalogo.ser";

    /**
     * Costruttore privato. In questo modo possiamo implementare il pattern SINGLETON.
     *  Verrà crerata un istanza unica della classe CatalogoModel, utilizzata per
     *  l'intera durata dell'esecuzione
     */
    private CatalogoModel() {
        super();
    }

    /**
     *  Restituisce un istanza della classe catalogo.
     * @return istanza di Catalogo
     */
    public static CatalogoModel getInstance() {
        if (instance == null) {
            instance = new CatalogoModel();
        }
        return instance;
    }

    /**
     * @return PATH al file in cui sono salvate le informazioni relative alle auto
     */
    @Override
    protected String getPathToData() {
        return CATATLOGO_PATH;
    }

    /**
    *   Override del metodo addData. In questo modo, al primo caricamento del model
     *   verranno salvatiin un set:
     *      - tutti gli ID già in uso
     *      - tutti i modelli usati
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

    /** Metodo per la generazione di codiciUnivoci, assegnabili alle macchine */
    public int getUniqueId(){
        int newId;
        //Continua a generare codici casuali, fino a che non ne trova uno nuovo > 0
        do {
            newId = UUID.randomUUID().hashCode();
        } while (usedIds.contains(newId) || newId <= 0);
        return newId;
    }

    /**
     * Controlla che il campo id non sia già presente all'interno del catalogo
     * @param id
     * @return  <u>TRUE</u>
     */
    public boolean checkId(int id){
        return !usedIds.contains(id);
    }

    /**
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

    /**
     *
     * @param min Prezzo MASSIMO
     * @param max Prezzo MASSIMO
     * @param data Lista di auto, da FILTRARE
     * @return  Restituisce una lista di auto, ordinate in base al prezzo
     */
    public static ArrayList<AutoNuova> filterAutoByPrice(String min, String max, ArrayList<AutoNuova> data) {
        //Ritorno tutte le auto con prezzo minore di maxPrice
        if(min.isEmpty()){
            return data.stream()
                    .filter(t ->  t.getCostoBase() < Integer.parseInt(max))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        //Ritorno le auto con prezzo maggiore di minPrice
        else if(max.isEmpty()){
            return data.stream()
                    .filter(t -> t.getCostoBase() > Integer.parseInt(min) )
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        //Entrambi i campi sono valorizzati
        return data.stream()
                .filter(t -> t.getCostoBase() > Integer.parseInt(min) && t.getCostoBase() < Integer.parseInt(max))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Ritorna l'insieme di Brand per cui è presente almeno un modello di Auto.
     * In questo modo è possibile popolare i filtri con i modelli strettamente necessari
     * @return Set dei brand in uso
     */
    public Set<Marca> getUsedBrands() {
        return usedBrands;
    }

    //Metodi GET e SET per selectedAuto
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
            addData(selectedAuto);
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
        setTempAuto(null);
    }

    //Metodi GET e SET per tempAuto
    public void setTempAuto(AutoNuova tempAuto) {
        this.tempAuto = tempAuto;
    }
    public AutoNuova getTempAuto(){
        return tempAuto;
    }
}
