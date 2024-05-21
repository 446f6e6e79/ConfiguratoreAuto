package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AutoNuova extends Auto implements Serializable {
    private int id;
    private String descrizione;
    private Dimensione dimensione;
    private double costoBase;
    //Array di 12 double, rappresentano ogni mese lo scontoApplicato
    private double [] scontoPerMese;
    private ArrayList<Motore> motoriDisponibili;
    private TreeSet<Optional> optionalDisponibili;

    public AutoNuova(int id, Marca marca, String modello, Dimensione dimensione, String descrizione, double costoBase, double [] scontoPerMese){
        super(marca, modello);
        this.id = id;
        this.dimensione = dimensione;
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.motoriDisponibili = new ArrayList<>();
        this.optionalDisponibili = new TreeSet<>();
    }

    public double getCostoBase() {
        return costoBase;
    }

    public TreeSet<Optional> getOptionalDisponibili() {
        return optionalDisponibili;
    }

    /**
     *
     * @param category Categoria di optional
     * @return ritorna tutti gli optional di tipo category, dispobili per tale modello
     */
    public ArrayList<Optional> getOptionalByCategory(TipoOptional category){
        return optionalDisponibili.stream().filter(optional -> optional.getCategoria().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *
     * @return Restituisce un arrayList contenente tutti i colori aggiunti per tale macchina
     */
    public ArrayList<String> getUsedColors(){
        HashSet<String> colors = new HashSet<>();
        for(Optional colore: getOptionalByCategory(TipoOptional.colore)){
            colors.add(colore.getDescrizione());
        }
        return new ArrayList<>(colors);
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

    public ArrayList<Motore> getMotoriDisponibili() {
        return motoriDisponibili;
    }

    public double[] getScontoPerMese() {
        return scontoPerMese;
    }

    public double getSconto(Date data){
        return this.getScontoPerMese()[data.getMonth()];
    }
    /*
    *   Due auto sono considerate uguali se condividono lo stesso id
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof AutoNuova other &&
                this.id == other.id;
    }

    public void addMotore(Motore motore){
        motoriDisponibili.add(motore);
    }

    public int getId() {
        return id;
    }

    /**
     * Calcola il costo Totale di un auto:
     * @param chosenOtionals lista di optional SELEZIONATI
     * @param data data in cui è calcolato il prezzo:
     *             - PREVENTIVO: data = data richiesta preventivo
     *             - Modifica auto: data = data attuale
     * @return double costo totale, calcolato come:
     *      costo di base + costo Optional (- valutazione usato) - sconto
     */
    public double getCostoTotale(ArrayList<Optional> chosenOtionals, Date data){
        double tot = this.costoBase;
        for(Optional optional : chosenOtionals){
            tot += optional.getCosto();
        }
        tot -= (tot*getSconto(data) / 100);
        return tot;
    }

    /**
     *
     * @param price double, prezzo da formattare
     * @return  ritorna il toString del prezzo passato come parametro, formattato secondo lo standard
     */
    public static String getPriceAsString(double price){
        NumberFormat euroFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        return euroFormat.format(price);
    }

    public String getBasePriceAsString(){
        return getPriceAsString(costoBase);
    }

    public String getDescrizione(){
        return descrizione;
    }

    public Dimensione getDimensione() {
        return dimensione;
    }
}
