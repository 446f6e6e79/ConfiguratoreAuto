package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AutoNuova extends Auto implements Serializable {
    private int id;
    private String descrizione;
    private double costoBase;
    //Array di 12 double, rappresentano ogni mese lo scontoApplicato
    private double [] scontoPerMese;
    private HashSet<Motore> motoriDisponibili;
    private TreeSet<Optional> optionalDisponibili;

    public AutoNuova(int id, Marca marca, String modello, Dimensione dimensione, String descrizione, double costoBase, double [] scontoPerMese){
        super(marca, modello, dimensione);
        this.id = id;
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.motoriDisponibili = new HashSet<>();
        this.optionalDisponibili = new TreeSet<>();
    }

    public double getCostoBase() {
        return costoBase;
    }

    public TreeSet<Optional> getOptionalDisponibili() {
        return optionalDisponibili;
    }
    public HashSet<Motore> getMotoriDisponibili() {
        return motoriDisponibili;
    }
    public double[] getScontoPerMese() {
        return scontoPerMese;
    }

    /*
    *   Due auto sono considerate uguali se condividono lo stesso id
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof AutoNuova other &&
                this.id == other.id;
    }
    public void addOptional(Optional optional){
        optionalDisponibili.add(optional);
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
    public double getCostoTotale(ArrayList<Optional> chosenOtionals){
        double tot = this.costoBase;
        for(Optional optional : chosenOtionals){
            tot += optional.getCosto();
        }
        tot -= (tot*scontoPerMese[new Date().getMonth()] / 100);
        return tot;
    }
    public String getPriceAsString(double price){
        NumberFormat euroFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        return euroFormat.format(costoBase);
    }
    public String getBasePriceAsString(){
        return getPriceAsString(costoBase);
    }

    public ArrayList<Optional> getOptionalByCategory(TipoOptional category){
        return optionalDisponibili.stream().filter(optional -> optional.getCategoria().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }
    public String getDescrizione(){
        return descrizione;
    }
}
