package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.TreeSet;

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

    public String getPriceAsString(){
        NumberFormat euroFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        return euroFormat.format(costoBase);
    }
}
