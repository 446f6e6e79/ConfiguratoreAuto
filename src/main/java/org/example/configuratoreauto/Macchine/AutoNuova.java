package org.example.configuratoreauto.Macchine;

import javafx.scene.image.Image;
import org.example.configuratoreauto.Preventivi.Preventivo;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class AutoNuova extends Auto implements Serializable {
    private int id;
    private String descrizione;
    private Dimensione dimensione;
    private double costoBase;
    /**Array di 12 double, rappresentano ogni mese lo scontoApplicato*/
    private int [] scontoPerMese;
    /** ArrayList contenente tutti i motori disponibili per il modello*/
    private ArrayList<Motore> motoriDisponibili = new ArrayList<>();
    private ArrayList<Optional> optionalDisponibili = new ArrayList<>();

    public AutoNuova(int id, Marca marca, String modello, Dimensione dimensione, String descrizione, double costoBase, int [] scontoPerMese){
        super(marca, modello);
        //Se id è già presente, segnalo l'errore
        this.id = id;
        this.dimensione = dimensione;
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.motoriDisponibili = new ArrayList<>();
        this.optionalDisponibili = new ArrayList<>();
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

    public AutoNuova(int id){
        this.id = id;
    }

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
        this.costoBase = costoBase;
    }

    //GET e SET per il campo dimensione
    public Dimensione getDimensione() {
        return dimensione;
    }
    public void setDimensione(Dimensione dimensione) {
        this.dimensione = dimensione;
    }

    //GET e SET descrizione
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    //GET e SET Sconto per mese
    public int[] getScontoPerMese() {
        return scontoPerMese;
    }
    public void setScontoPerMese(int[] scontoPerMese) {
        this.scontoPerMese = scontoPerMese;
    }

    //GET e SET motori disponibili
    public ArrayList<Motore> getMotoriDisponibili() {
        return motoriDisponibili;
    }
    public void setMotoriDisponibili(ArrayList<Motore> motoriDisponibili){
        this.motoriDisponibili = motoriDisponibili;
    }

    //GET E SET OPTIONAL
    public ArrayList<Optional> getOptionalDisponibili() {
        return optionalDisponibili;
    }
    public void setOptionalDisponibili(ArrayList<Optional> optionalDisponibili){
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
     * Aggiunge un motore a quelli disponibili.
     * Se tale motore era già presente, lo sostituisce con quello nuovo
     * @param motore motore da aggiungere alla lista
     */
    public void addMotore(Motore motore){
        motoriDisponibili.add(motore);
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
    public ArrayList<Immagine> getImageByColor(String color){
        return super.getImmagini().stream()
                .filter(t-> t.getColor().equals(color))
                .collect(Collectors.toCollection(ArrayList::new));
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


    /*
    *   Due auto sono considerate uguali se condividono lo stesso id
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof AutoNuova other &&
                this.id == other.id;
    }

    public String getBasePriceAsString(){
        return Preventivo.getPriceAsString(costoBase);
    }

    /**
     * Metodo che permette di ottenere l'immagine di default di un auto nuova
     * @return Restituisce l'immagine di DEFAULT dell'auto
     */
    public Image getDefaultImage(String colore){
        if(colore == null){
            //throw new IllegalArgumentException("Il colore non può essere nullo");
        }
        //ArrayList<Immagine> immagini = getImageByColor(colore);

        //FIX TEMP:
        ArrayList<Immagine> immagini = getImmagini();

        //Se è presente almeno un immagine per quel colore, la restituisco
        if(!immagini.isEmpty()){
            return this.getImmagini().get(0).getImage();
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
