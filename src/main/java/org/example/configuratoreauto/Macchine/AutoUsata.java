package org.example.configuratoreauto.Macchine;

import java.io.Serializable;

public class AutoUsata extends Auto implements Serializable {

    private String targa;
    private int km;
    private Motore motore;

    public AutoUsata(Marca marca, String modello, Dimensione dimensione, Motore motore, String targa, int km) {
        super(marca, modello, dimensione);
        this.targa=targa;
        this.km=km;
        this.motore=motore;
    }
    /*
    *   Consideriamo equals due auto usate che condividono la stessa targa
    * */
    @Override
    public boolean equals(Object o) {
        return o instanceof AutoUsata other &&
                this.targa.equals(other.targa);
    }
}
