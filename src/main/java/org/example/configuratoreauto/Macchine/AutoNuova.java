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

    public ArrayList<Optional> getOptionalByCategory(TipoOptional category){
        return optionalDisponibili.stream().filter(optional -> optional.getCategoria().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addOptional(Optional optional){
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

    /*Calcola il costo Totale di un auto:
     *  Parametri: optionalRichiesti
     * Il costo è calcolato come:
     *   - costo di base + costo Optional (- valutazione usato) - sconto
     */
    public double getCostoTotale(ArrayList<Optional> chosenOtionals, Date data){
        double tot = this.costoBase;
        for(Optional optional : chosenOtionals){
            tot += optional.getCosto();
        }
        tot -= (tot*scontoPerMese[data.getMonth()] / 100);
        return tot;
    }
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
