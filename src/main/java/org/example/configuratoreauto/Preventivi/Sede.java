package org.example.configuratoreauto.Preventivi;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Sede implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * La sede si definisce uguale se ha lo stesso nome ed indirizzo
     * @param o oggetto sede che si vuole confrontare
     * @return
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Sede s &&
                this.indirizzo.equals(s.indirizzo) &&
                this.nome.equals(s.nome);
    }

    /**
     * La sede è resa univoca dal suo nome ed indirizzo
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(nome, indirizzo);
    }

    @Override
    public String toString() {
        return nome + " " + indirizzo;
    }
}
