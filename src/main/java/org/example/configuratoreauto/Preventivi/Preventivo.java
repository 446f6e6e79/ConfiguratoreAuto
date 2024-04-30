package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Utenti.Cliente;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private Date scadenza;
    private double valutazione;
    public Preventivo(AutoUsata usata, AutoNuova acquisto, Sede sede, Cliente cliente){
        this.data = new Date(); //Recupera la data attuale
        //Se non è inserita un auto usata, il preventivo è già finalizzato
        if(usata == null){
            this.stato = StatoPreventivo.FINALIZZATO;
            setScadenza();//Se finalizzato allora setta la scadenza
        }
        else{
            //Altrimenti il preventivo dovrà essere finalizzato da un impiegato
            this.stato = StatoPreventivo.RICHIESTO;
        }
        this.usata = usata;
        this.acquisto = acquisto;
        this.sede = sede;
        this.cliente = cliente;
        setConsegna();
    }

    /*
    *  Calcola la data di consegna effettiva della macchina.
    *  La data è calcolata come:
    *     - data base + 1 mese + (10 giorni * numeroOptional)
    * */
    public void setConsegna() {
        Calendar dataDiConsegna = Calendar.getInstance();
        dataDiConsegna.setTime(data);
        dataDiConsegna.add(Calendar.MONTH, 1);
        dataDiConsegna.add(Calendar.DAY_OF_MONTH, 10 * optionals.size());
        this.consegna = dataDiConsegna.getTime();
    }

    //Imposta la data di scadenza effettiva dalla data attuale
    public void setScadenza() {
        Calendar scadenza = Calendar.getInstance();
        scadenza.setTime(new Date());
        scadenza.add(Calendar.DAY_OF_MONTH, 20);
        this.scadenza = scadenza.getTime();
    }

    /*
    Verifica che il preventivo non sia scaduto. Si ritiene scaduto da 20 giorni dalla finalizzazione
     */
    public boolean isScaduto(){
        if((new Date()).after(scadenza)){
            return true;
        }
        return false;
    }


    //Ritorna la data nel formato DD/MM/YYYY
    private String getDataAsString(Date d){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public String getDataPreventivoAsString(){
        return getDataAsString(this.data);
    }

    public String getDataConsegnaAsString(){
        return getDataAsString(this.consegna);
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

    public void setValutazione(double valutazione) {
        this.valutazione = valutazione;
    }

    public Sede getSede() {
        return sede;
    }

    /*
    Modifica lo stato del preventivo, attuandone alcune verifiche
     */
    public void changeStato(StatoPreventivo stato) {
        if(stato.equals(StatoPreventivo.FINALIZZATO)){//Se il preventivo viene finalizzato viene impostata la scadenza
            setScadenza();
        }
        this.stato = stato;
    }

    /*Calcola il costo Totale del Preventivo
    * Il costo è calcolato come:
    *   - costo di base + costo Optional - valutazione usato - sconto
    *   - */
    public double costoTotale(){
        double tot = acquisto.getCostoBase();
        for(Optional optional : this.optionals){
            tot += optional.getCosto();
        }
        if(usata!=null && stato!=StatoPreventivo.RICHIESTO){
            tot-=valutazione;
        }
        return tot-(tot*acquisto.getScontoPerMese()[data.getMonth()]);
    }
}
