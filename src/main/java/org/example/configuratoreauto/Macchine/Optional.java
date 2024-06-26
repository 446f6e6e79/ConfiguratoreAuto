package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.Preventivi.Preventivo;

import java.io.Serial;
import java.io.Serializable;

public class Optional implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private TipoOptional categoria;
    private String descrizione;
    private double costo;

    /**
     * Costruttore per la classe optional. Verifica che i campi siano effettivamente
     * validi, prima di creare l'oggetto
     * @param categoria categoria a cui appartiene
     * @param descrizione descrizione dell'optional
     * @param costo prezzo dell'optional
     * @throws IllegalArgumentException se uno dei campi non è valido
     */
    public Optional( TipoOptional categoria, String descrizione, double costo){
        if(categoria == null){
            throw new IllegalArgumentException("Categoria non valida");
        }
        this.categoria = categoria;

        if(descrizione == null || descrizione.isEmpty()){
            throw new IllegalArgumentException("Descrizione non valida");
        }
        this.descrizione = descrizione;

        if(costo < 0){
            throw new IllegalArgumentException("Costo non valido");
        }
        this.costo = costo;
    }

    //Metodi get per i campi della classe OPTIONAL
    public double getCosto() {
        return costo;
    }
    public TipoOptional getCategoria() {
        return categoria;
    }
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Consideriamo uguali due oggetti optional che condividono:
     *  1)stessa categoria
     *  2)stessa descrizione
     */
    @Override
    public boolean equals(Object o){
        return o instanceof Optional other &&
                this.categoria == other.categoria &&
                this.descrizione.equals(other.descrizione);
    }

    public String toString(){
        return descrizione +": "+ Preventivo.getPriceAsString(costo);
    }
}
