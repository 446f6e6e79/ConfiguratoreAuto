package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

public class Preventivo implements Serializable {

    private Date data;
    private Date consegna;
    private StatoPreventivo stato;
    private AutoUsata usata;
    private AutoNuova acquisto;
    private HashSet<Optional> optionals;
    private Sede sede;
    private Cliente cliente;
    private Impiegato impiegato;
    public Preventivo(Date data, Date consegna, StatoPreventivo stato, AutoUsata usata, AutoNuova acquisto, Sede sede, Cliente cliente, Impiegato impiegato){
        this.data = data;
        this.consegna = consegna;
        this.stato = stato;
        this.usata = usata;
        this.acquisto = acquisto;
        this.sede = sede;
        this.impiegato = impiegato;
        this.cliente = cliente;
    }


    public void checkOptional(){

    }
    public Date getConsegna() {
        return consegna;
    }

    public Date getData() {
        return data;
    }

    public StatoPreventivo getStato() {
        return stato;
    }

    public AutoUsata getUsata() {
        return usata;
    }

    public AutoNuova getAcquisto() {
        return acquisto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Impiegato getImpiegato() {
        return impiegato;
    }

    public Sede getSede() {
        return sede;
    }

    public void changeStato(StatoPreventivo stato) {
        this.stato = stato;
    }

    public double costoTotale(){
        double tot = acquisto.getCostoBase();
        for(Optional optional : this.optionals){
            tot += optional.getCosto();
        }
        if(usata!=null && stato!=StatoPreventivo.RICHIESTO){
            //tot-= valutazione impiegato
        }
        return tot-(tot*acquisto.getScontoPerMese()[data.getMonth()]);
    }
}
