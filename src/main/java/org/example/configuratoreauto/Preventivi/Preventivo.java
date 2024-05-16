package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Motore;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Utenti.Cliente;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class Preventivo implements Serializable, Comparable<Preventivo>{
    private Date data;
    private Date consegna;
    private StatoPreventivo stato;
    private AutoUsata usata;
    private AutoNuova acquisto;
    private HashSet<Optional> optionals = new HashSet<>();
    private Motore motoreScelto;
    private Sede sede;
    private Cliente cliente;
    private Date scadenza;
    private double valutazione;

    public Preventivo(AutoUsata usata, AutoNuova acquisto, Sede sede, Cliente cliente, Date d){
        this.data = d;
        this.usata = usata;

        if(usata == null){
            //In caso non sia presente un auto usata, il preventivo è già finalizzato e posso impostare una scadenza
            this.stato = StatoPreventivo.FINALIZZATO;
            setScadenza(new Date());
        }
        else{
            this.stato = StatoPreventivo.RICHIESTO;
        }

        this.acquisto = acquisto;
        this.sede = sede;
        this.cliente = cliente;
        setConsegna();
    }

    /*
    *   Costruttore Preventivo, setta la data di esecuzione del preventivo, alla data attuale
    * */
    public Preventivo(AutoUsata usata, AutoNuova acquisto, Sede sede, Cliente cliente){
        this(usata, acquisto, sede, cliente, new Date());
    }

    /*
        Costruttore Preventivo senza auto Usata:
            - imposta di default la data a quella corrente
            - imposta lo stato del preventivo a FINALIZZATO
    */

    public Preventivo(AutoNuova acquisto, Sede sede, Cliente cliente){
        this(null, acquisto, sede, cliente);
    }


    /*
    *  Calcola la data di consegna effettiva della macchina.
    *  La data è calcolata come:
    *     - data base + 1 mese + (10 giorni * numeroOptional)
    * */
    private void setConsegna() {
        Calendar dataDiConsegna = Calendar.getInstance();
        dataDiConsegna.setTime(data);
        dataDiConsegna.add(Calendar.MONTH, 1);
        if(optionals != null){
            dataDiConsegna.add(Calendar.DAY_OF_MONTH, 10 * optionals.size());
        }
        this.consegna = dataDiConsegna.getTime();
    }

    /*
    *   Imposta la data di scadenza a partire da 20 giorni dalla data passata come parametro. Si verificano 2 casi:
    *       1) Preventivo senza autoUsata -> la scadenza è impostata a 20 giorni dal giorno in cui il preventivo è richiesto
    *       2) Preventivo con autoUsata -> scadenza impostata a 20 giorni dalla finalizzazione
    * */
    private void setScadenza(Date d) {
        Calendar scadenza = Calendar.getInstance();
        scadenza.setTime(data);
        scadenza.add(Calendar.DAY_OF_MONTH, 20);
        this.scadenza = scadenza.getTime();
    }

    /*
        Verifica che il preventivo non sia scaduto. Un preventivo è scaduto dopo 20 giorni dalla finalizzazione
     */
    private boolean isScaduto(){
        if(new Date().after(scadenza)){
            return true;
        }
        return false;
    }

    /*
    *   Verifica se la data di consena è passata. In tal caso dovrà essere cambiato lo stato del preventivo
    * */
    private boolean isDisponibileAlRitiro(){
        if(new Date().after(consegna)){
            return true;
        }
        return false;
    }

    //Ritorna la data nel formato DD/MM/YYYY
    private String getDataAsString(Date d){
        return new SimpleDateFormat("dd-MM-yyyy").format(d);
    }

    public String getDataPreventivoAsString(){
        return getDataAsString(this.data);
    }

    public String getDataConsegnaAsString(){
        return getDataAsString(this.consegna);
    }

    public String getDataScadenzaAsString(){
        return getDataAsString(this.scadenza);
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

    /*
    *   Set della valutazione dell'usato. Una volta valutata verrano svolte automaticamente le seguenti operazioni:
    *       - stato impostato a FINALIZZATO
    *       - scadenza impostata a 20 giorna dalla data attuale
    * */
    public void setValutazione(double valutazione) {
        this.valutazione = valutazione;
        setStato(StatoPreventivo.FINALIZZATO);
        setScadenza(new Date());
    }
    public Sede getSede() {
        return sede;
    }

    public void setStato(StatoPreventivo stato) {
        this.stato = stato;
    }

    /*
     *   Aggiorna automaticamente lo stato del preventivo:
     *      -Se sono passati 20 giorni, e non è stato pagato il preventivo, viene assegnato lo stato SCADUTO
     *      -Se è stata superata la data del RITIRO ed il preventivo risulta PAGATO, assegnato lo stato DISPONIBILE_AL_RITIRO
     *
     *   Tale funzione sarà chiamata ogni qualvolta viene caricato il RegistroModel, tenendo aggiornati i dati
     * */
    public void updateStatoAutomatico(){
        if(stato == StatoPreventivo.FINALIZZATO && isScaduto()){
            setStato(StatoPreventivo.SCADUTO);
        }
        if(stato == StatoPreventivo.PAGATO && isDisponibileAlRitiro()){
            setStato(StatoPreventivo.DISPONIBILE_AL_RITIRO);
        }
    }

    /*Calcola il costo Totale del Preventivo
    * Il costo è calcolato come:
    *   - costo di base + costo Optional (- valutazione usato) - sconto
    */
    public double getCostoTotale(){
        double tot = acquisto.getCostoBase();
        for(Optional optional : this.optionals){
            tot += optional.getCosto();
        }
        tot -= (tot*acquisto.getScontoPerMese()[data.getMonth()]);
        if(usata != null && stato != StatoPreventivo.RICHIESTO){
            tot -= valutazione;
        }
        return tot;
    }

    public String toString(){
        return "AUTO: "+this.acquisto.toString()+"\n"+
                "Data effettuato: " +getDataPreventivoAsString() +"\n"+
                "Data scadenza: " +getDataScadenzaAsString() +"\n"+
                "Data consegna: " +getDataConsegnaAsString() +"\n"+
                "STATO:" +getStato();
    }
    @Override
    public boolean equals(Object o){
        return o instanceof Preventivo other &&
                this.acquisto.equals(other.acquisto) &&
                ((this.usata == null && other.usata == null) || this.usata.equals(other.usata) ) &&
                this.cliente.equals(other.cliente) &&
                ((this.optionals == null && other.optionals == null) || this.optionals.equals(other.optionals) ) &&
                this.sede.equals(other.sede);
    }

    @Override
    public int compareTo(Preventivo o) {
        return this.data.compareTo(o.data);
    }
}
