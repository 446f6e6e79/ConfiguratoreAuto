package org.example.configuratoreauto.Macchine;

import java.util.HashSet;

public class AutoNuova extends Auto {

    private String descrizione;
    private double costoBase;
    private double scontoPerMese;
    private HashSet<Optional> optionals;
    public AutoNuova(Marca marca, String id, Dimensione dimensione, Motore motore, String descrizione, double costoBase, double scontoPerMese) {
        super(marca, id, dimensione, motore);
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.optionals = new HashSet<>();
    }

    public void addOptional(Optional optional){
        optionals.add(optional);
    }
}
