package org.example.configuratoreauto.Macchine;

import java.io.Serializable;
import java.util.HashSet;

public class AutoNuova extends Auto implements Serializable {

    private String descrizione;
    private double costoBase;
    //Array di 12 double, rappresentano ogni mese lo scontoApplicato
    private double [] scontoPerMese;
    private HashSet<Optional> optionalDisponibili;

    public AutoNuova(Marca marca, String modello, Dimensione dimensione, Motore motore, String descrizione, double costoBase, double [] scontoPerMese) {
        super(marca, modello, dimensione, motore);
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.optionalDisponibili = new HashSet<>();
    }

    public double getCostoBase() {
        return costoBase;
    }

    public double[] getScontoPerMese() {
        return scontoPerMese;
    }

    public void addOptional(Optional optional){
        optionalDisponibili.add(optional);
    }
}