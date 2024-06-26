package org.example.configuratoreauto.Macchine;

import javafx.scene.image.Image;
import org.example.configuratoreauto.Preventivi.Preventivo;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class AutoNuova extends Auto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String descrizione;
    private Dimensione dimensione;
    private double costoBase;
    /**Array di 12 double, rappresentano ogni mese lo scontoApplicato*/
    private int [] scontoPerMese;
    /** ArrayList contenente tutti i motori disponibili per il modello*/
    private ArrayList<Motore> motoriDisponibili = new ArrayList<>();
    private ArrayList<Optional> optionalDisponibili = new ArrayList<>();


    /**
     *  Costruttore dell'oggetto di classe AutoNuova
     * @throws IllegalArgumentException se l'id selezionato è già in uso
     */
    public AutoNuova(int id){
        //Se l'id generato non è valido:
        if(!CatalogoModel.getInstance().checkId(id)){
            throw new IllegalArgumentException("Id non valido!");
        }
        this.id = id;
    }

    /**
     *  Crea una nuova istanza classe Auto, copia di quella passata come parametro
     * */
    public AutoNuova(AutoNuova modelToCopy) {
        super(modelToCopy.getMarca(), modelToCopy.getModello());
        this.id = modelToCopy.getId();
        this.descrizione = modelToCopy.getDescrizione();
        this.dimensione = modelToCopy.getDimensione();
        this.costoBase = modelToCopy.getCostoBase();
        this.scontoPerMese = Arrays.copyOf(modelToCopy.getScontoPerMese(), modelToCopy.getScontoPerMese().length);
        this.motoriDisponibili = new ArrayList<>(modelToCopy.getMotoriDisponibili());
        this.optionalDisponibili = new ArrayList<>(modelToCopy.getOptionalDisponibili());
        super.setImmagini(new ArrayList<>(modelToCopy.getImmagini()));
    }


    /* COSTRUTTORE DA DEMOLIRE NON APPENA FINITA FASE DI TESTING
    public AutoNuova(int id, Marca marca, String modello, Dimensione dimensione, String descrizione, double costoBase, int[] scontoPerMese) {
        super(marca, modello);
        this.id = id;
        this.dimensione = dimensione;
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.motoriDisponibili = new ArrayList<>();
        this.optionalDisponibili = new ArrayList<>();
    }
    */

    /**
     * Costruttore usato per generare un auto TEMPORANEA
     */
    protected AutoNuova(){}

    //Metodo GET per il campoID
    public int getId() {
        return id;
    }


    //GET e SET costoBase
    public double getCostoBase() {
        return costoBase;
    }
    public void setCostoBase(double costoBase) {
        if(costoBase <= 0){
            throw new IllegalArgumentException("Costo base non valido!");
        }
        this.costoBase = costoBase;
    }

    //GET e SET per il campo dimensione
    public Dimensione getDimensione() {
        return dimensione;
    }
    public void setDimensione(Dimensione dimensione) {
        if(dimensione == null){
            throw new IllegalArgumentException("Dimensione non valida!");
        }
        this.dimensione = dimensione;
    }

    //GET e SET descrizione
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        if(descrizione == null || descrizione.isEmpty()){
            throw new IllegalArgumentException("Descrizione non valida!");
        }
        this.descrizione = descrizione;
    }

    //GET e SET Sconto per mese
    public int[] getScontoPerMese() {
        return scontoPerMese;
    }
    public void setScontoPerMese(int[] scontoPerMese) {
        //Controllo che non sian presenti sconti negativi o che eccedano il 100%
        if(scontoPerMese.length != 12){
            throw new IllegalArgumentException("Sconto non valido!");
        }
        for(int sconto: scontoPerMese){
            if (sconto < 0 || sconto > 100){
                throw new IllegalArgumentException("Sconto non valido!");
            }
        }
        this.scontoPerMese = scontoPerMese;
    }

    //GET e SET motori disponibili
    public ArrayList<Motore> getMotoriDisponibili() {
        return motoriDisponibili;
    }
    public void setMotoriDisponibili(ArrayList<Motore> motoriDisponibili){
        if(motoriDisponibili == null || motoriDisponibili.isEmpty()){
            throw new IllegalArgumentException("Non è presente alcun motore!");
        }
        this.motoriDisponibili = motoriDisponibili;
    }

    /**
     * Aggiunge un motore a quelli disponibili.
     * Se tale motore era già presente, lo sostituisce con quello nuovo
     * @param motore motore da aggiungere alla lista
     * @throws IllegalArgumentException se è aggiunto un dato non valido
     */
    public void addMotore(Motore motore){
        if(motore == null){
            throw new IllegalArgumentException("Motore aggiunto non valido");
        }
        motoriDisponibili.add(motore);
    }

    //GET E SET OPTIONAL
    public ArrayList<Optional> getOptionalDisponibili() {
        return optionalDisponibili;
    }
    public void setOptionalDisponibili(ArrayList<Optional> optionalDisponibili){
        if(optionalDisponibili == null || optionalDisponibili.isEmpty()){
            throw new IllegalArgumentException("Non è presente alcun optional!");
        }
        this.optionalDisponibili = optionalDisponibili;
    }

    /**
     * Aggiunge un nuovo optional a quelli dipsonibili.
     * Se tale optional era già presente, lo sostituisce con quello nuovo
     * @param optional optioanl da aggiungere alla lista
     */
    public void addOptional(Optional optional){
        //Provo a rimuovere l'optional, se già presente
        optionalDisponibili.remove(optional);
        //Aggiungo l'optional
        optionalDisponibili.add(optional);
    }


    /**
     * Data una specifica categoria, ritorna tutti gli optional disponibili per essa
     * @param category Categoria di optional
     * @return ritorna tutti gli optional di tipo category, dispobili per tale modello
     */
    public ArrayList<Optional> getOptionalByCategory(TipoOptional category){
        return optionalDisponibili.stream().filter(optional -> optional.getCategoria().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param data Una certa data
     * @return sconto percentuale valido per la data inserita
     */
    public int getSconto(Date data){
        return this.getScontoPerMese()[data.getMonth()];
    }

    /**
     * Calcola il costo Totale di un auto:
     * @param chosenOtionals lista di optional SELEZIONATI
     * @param data data in cui è calcolato il prezzo:
     *             - PREVENTIVO: data = data richiesta preventivo
     *             - Modifica auto: data = data attuale
     * @return double costo totale, calcolato come:
     *      costo di base + costo Optional - sconto
     */
    public double getCostoTotale(ArrayList<Optional> chosenOtionals, Date data){
        double tot = getPrezzoNoSconto(chosenOtionals);
        return tot - (tot*getSconto(data) / 100);
    }


    /**
     * Calcola il costo Totale di un auto, privo di sconti:
     * @param chosenOtionals lista di optional SELEZIONATI
     * @return double costo totale, calcolato come:
     *      costo di base + costo Optional
     */
    public double getPrezzoNoSconto(ArrayList<Optional> chosenOtionals){
        double tot = this.costoBase;
        for(Optional optional : chosenOtionals){
            tot += optional.getCosto();
        }
        return tot;
    }

    /**
     * Due auto sono considerate uguali se condividono lo stesso ID.
     * In questo modo possiamo verificarne l'univocità.
     */
    @Override
    public boolean equals(Object o){
        return o instanceof AutoNuova other &&
                this.id == other.id;
    }

    /**
     *  Restituisce il campo CostoBase, formattato
     * @return Stringa che rappresenta il costo base, formattato
     */
    public String getBasePriceAsString(){
        return Preventivo.getPriceAsString(costoBase);
    }

    /**
     * @return Restituisce un arrayList contenente tutti i colori aggiunti per tale macchina
     */
    public ArrayList<String> getUsedColors() {
        return getOptionalByCategory(TipoOptional.Colore)
                .stream()
                .map(Optional::getDescrizione)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *  Restituisce tutte le immagini presenti per il modello,
     * @param color colore
     * @return  Lista di immagini Auto, relative al colore
     */
    public ArrayList<Immagine> getImagesByColor(String color){
        return super.getImmagini().stream()
                .filter(t-> t.getColor().equals(color))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
        Restituisce l'optional COLORE di default. Consideriamo come colore di DEFAULT:
        <p>- primo colore con prezzo = 0 nella lista
        <p>- colore con prezzo più basso nella lista
        <p>Tale metodo restituisce <u>NULL</u> se non è stato trovato alcun colore tra gli optional
    */
    public Optional getDefaultColor(){
        List<Optional> availableColors = this.getOptionalByCategory(TipoOptional.Colore);
        Optional defaultColor = null;
        if (!availableColors.isEmpty()){
            for (Optional color : availableColors) {
                //Se trovo un colore con prezzo = 0, ho finito
                if (color.getCosto() == 0) {
                    return color;
                }
                //Se trovo un colore con prezzo ancora minore, aggiorno il default color
                else if (defaultColor == null || color.getCosto() < defaultColor.getCosto()) {
                    defaultColor = color;
                }
            }
        }
        //Restituisce null
        return defaultColor;
    }

    /**
     * Metodo che permette di ottenere l'immagine di default di un auto nuova
     * @return Restituisce l'immagine di DEFAULT dell'auto
     */
    public Image getDefaultImage(String colore){
        if(colore != null) {
            ArrayList<Immagine> imgColor = getImagesByColor(colore);
            //Se è presente almeno un immagine per quel colore, la restituisco
            if (!imgColor.isEmpty()) {
                return imgColor.get(0).getImage();
            }
        }
            return new Image(getClass().getResourceAsStream("/img/no_data.png"));
    }
    
    /**
     * Salva tutte le immagini, memorizzate nell'arrayList in memoria interna del progetto
     */
    @Override
    public void addToLocalImages() {
        //Aggiungo tutte le immagini relative alla macchina nella cartella temporanea
        for (Immagine img : super.getImmagini()) {
            img.addAutoNuova(this);
        }

        //Sposto la directory tempImages carImages/auto.getId
        try {
            Immagine.cleanAndRenameDirectory(this);
        } catch (IOException e) {
            System.err.println("Error moving images to target directory: " + e.getMessage());
        }
    }

    /**
     * Compara le auto in base al loro costo base.
     * Di base ordina per prezzo CRESCENTE. E' possibile ottenere la lista
     * in ordine deCrescente, sfruttando il metodo reversed
     */
    public static Comparator<AutoNuova> getComparatorByPrice(){
        return new Comparator<>() {

            //Ordino in
            @Override
            public int compare(AutoNuova o1, AutoNuova o2) {
                return Double.compare(o1.getCostoBase(), o2.getCostoBase());
            }

            //Permette l'ordinamento decrescente
            @Override
            public Comparator<AutoNuova> reversed() {
                return Comparator.super.reversed();
            }
        };
    }
}
