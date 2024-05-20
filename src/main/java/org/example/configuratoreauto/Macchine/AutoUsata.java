package org.example.configuratoreauto.Macchine;

import java.io.Serializable;

public class AutoUsata extends Auto implements Serializable {

    private String targa;
    private int km;

    public AutoUsata(Marca marca, String modello, String targa, int km) {
        super(marca, modello);
        this.targa=targa;
        this.km=km;
    }
    /*
    *   Consideriamo equals due auto usate che condividono la stessa targa
    * */
    @Override
    public boolean equals(Object o) {
        return o instanceof AutoUsata other &&
                this.targa.equals(other.targa);
    }

    public String getTarga() {
        return targa;
    }
}
