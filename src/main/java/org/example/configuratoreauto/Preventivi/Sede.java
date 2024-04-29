package org.example.configuratoreauto.Preventivi;

import java.io.Serializable;
import java.util.Objects;

public class Sede implements Serializable {

    private String nome;
    private String indirizzo;
    public Sede(String nome, String indirizzo){
        this.indirizzo = indirizzo;
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sede sede = (Sede) o;
        return Objects.equals(nome, sede.nome) && Objects.equals(indirizzo, sede.indirizzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, indirizzo);
    }
}
