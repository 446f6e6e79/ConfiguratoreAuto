package org.example.configuratoreauto.Macchine;

import java.io.Serializable;

public class AutoUsata extends Auto implements Serializable {

    private String targa;
    private int km;

    public AutoUsata(Marca marca, String id, Dimensione dimensione, Motore motore, String targa, int km) {
        super(marca, id, dimensione, motore);
        this.targa=targa;
        this.km=km;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && this.targa.equals(((AutoUsata) o).targa);
    }
}
