package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.Motore;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Utenti.Cliente;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Preventivo implements Serializable, Comparable<Preventivo>{
    private Date data;
    private Date consegna;
    private StatoPreventivo stato;
    private AutoUsata usata;
    private AutoNuova acquisto;
    private ArrayList<Optional> optionals = new ArrayList<>();
    private Motore motoreScelto;
    private Sede sede;
    private Cliente cliente;
    private Date scadenza;
    private double valutazione;

    public Preventivo(AutoNuova acquisto, Sede sede, Cliente cliente, Date d, Motore motore, ArrayList optionalScelti){
        this.data = d;
        this.stato = StatoPreventivo.RICHIESTO;
        this.acquisto = acquisto;
        this.sede = sede;
        this.cliente = cliente;
        this.motoreScelto = motore;
        if(optionalScelti != null){
            this.optionals.addAll(optionalScelti);
        }
    }

    public Preventivo(AutoNuova acquisto, Sede sede, Cliente cliente, Motore motore, ArrayList optionalScelti){
        this(acquisto, sede, cliente, new Date(), motore, optionalScelti);
    }

    /**
     *
     * @param price double, prezzo da formattare
     * @return  ritorna il toString del prezzo passato come parametro, formattato secondo lo standard
     */
    public static String getPriceAsString(double price){
        NumberFormat euroFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        return euroFormat.format(price);
    }

    public void setUsata(AutoUsata usata){
        this.usata = usata;
        if(usata == null){
            //In caso non sia presente un auto usata, il preventivo è già finalizzato e posso impostare una scadenza
            this.stato = StatoPreventivo.FINALIZZATO;
            setScadenza(new Date());
            setConsegna();
        }
    }

    /**
    *  Calcola la data di consegna effettiva della macchina.
    *  La data è calcolata come:
    *     - data richiesta + 1 mese + (10 giorni * numeroOptional)
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

    /**
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

    public void setMotoreScelto(Motore motoreScelto) {//Provvisorio
        this.motoreScelto = motoreScelto;
    }

    public Motore getMotoreScelto() {
        return motoreScelto;
    }

    /**
        Verifica che il preventivo non sia scaduto. Un preventivo è considerato scaduto dopo 20 giorni dalla finalizzazione
    */
    private boolean isScaduto(){
        if(new Date().after(scadenza)){
            return true;
        }
        return false;
    }

    /**
     * @param d Data che vogliamo formattare
     * @return  Data, nel formato dd-MM-yyyy
     */
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

    public double getValutazione() {
        return valutazione;
    }

    /**
    *   Set della valutazione dell'usato. Una volta valutata verrano svolte automaticamente le seguenti operazioni:
    *       - stato impostato a FINALIZZATO
    *       - scadenza impostata a 20 giorna dalla data attuale
    * */
    public void setValutazione(double valutazione) {
        this.valutazione = valutazione;
        setStato(StatoPreventivo.FINALIZZATO);
        setScadenza(new Date());
        setConsegna();
    }

    public ArrayList<Optional> getOptionalScelti() {
        return optionals;
    }

    public Sede getSede() {
        return sede;
    }

    public Date getData() {
        return data;
    }

    public void setStato(StatoPreventivo stato) {
        this.stato = stato;
    }

    /*
     *   Aggiorna automaticamente lo stato del preventivo:
     *      -Se sono passati 20 giorni, e non è stato pagato il preventivo, viene assegnato lo stato SCADUTO
     *   Tale funzione sarà chiamata ogni qualvolta viene caricato il RegistroModel, tenendo aggiornati i dati
     * */
    public void updateStatoAutomatico(){
        if(stato == StatoPreventivo.FINALIZZATO && isScaduto()){
            setStato(StatoPreventivo.SCADUTO);
        }
//        if(stato == StatoPreventivo.PAGATO && isDisponibileAlRitiro()){
//            setStato(StatoPreventivo.DISPONIBILE_AL_RITIRO);
//        }
    }

    /**
    *    Calcola il costo Totale del Preventivo
    * Il costo è calcolato come la sottrazione tra:
    *   - costo tot (calcolato nella classe auto)
    *   - valutazione usato
    */
    public double getCostoTotale(){
        double tot = this.acquisto.getCostoTotale(this.optionals, this.data);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preventivo that = (Preventivo) o;
        return Objects.equals(acquisto, that.acquisto) &&
                Objects.equals(usata, that.usata) &&
                Objects.equals(cliente, that.cliente) &&
                Objects.equals(optionals, that.optionals) &&
                Objects.equals(sede, that.sede);
    }


    /**
     *  Ordina gli oggetti preventivo in base al loro stato.
     *  A parità di stato, ordina secondo la data
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Preventivo o) {
        int diff = this.stato.compareTo(o.stato);
        if(diff != 0){
            return diff;
        }
        return this.data.compareTo(o.data);
    }
}
