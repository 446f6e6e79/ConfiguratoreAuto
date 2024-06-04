package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Macchine.Optional;
import org.example.configuratoreauto.Utenti.Cliente;

import java.io.Serial;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Preventivo implements Serializable, Comparable<Preventivo>{
    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * Costruttore della classe preventivo, usato solamente per testing. Permette di creare un preventivo
     * in una data, passata come parametro
     */
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

    /**
     *  Metodo per l'aggiunta di un auto usata al preventivo.
     *  In caso non sia stata aggiunta alcuna auto (usata = NULL), il preventivo viene impostato a FINALIZZATO e vengono impostate:
     *      -data di scadenza del preventivo
     *      -data di consegna
     * @param usata Auto usata, da aggiungere al preventivo
     */
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
     * Sconto è in data in cui il preventivo è stato richiesto.
     * @return Ritorna la percentuale di sconto dell'auto richiesta.
     *
     */
    public double getScontoAuto() {
        return acquisto.getSconto(data);
    }

    public String getScontoAutoFormatted(){
        return getPriceAsString(acquisto.getCostoTotale(optionals, data) - acquisto.getPrezzoNoSconto(optionals));
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
            long count = optionals.stream()
                    .filter(optional -> !(optional.getCosto() == 0 && optional.getCategoria() == TipoOptional.Colore))
                    .count();
            dataDiConsegna.add(Calendar.DAY_OF_MONTH, 10 * (int) count);
        }
        this.consegna = dataDiConsegna.getTime();
    }

    /**
    *   Imposta la data di scadenza a partire da 20 giorni dalla data passata come parametro. Si verificano 2 casi:
    *       1) Preventivo senza autoUsata -> la scadenza è impostata a 20 giorni dal giorno in cui il preventivo è richiesto
    *       2) Preventivo con autoUsata -> scadenza impostata a 20 giorni dalla finalizzazione
    * */
    private void setScadenza(Date d) {
        if(d == null){
            throw new IllegalArgumentException("Scadenza nulla inserito");
        }
        Calendar scadenza = Calendar.getInstance();
        scadenza.setTime(data);
        scadenza.add(Calendar.DAY_OF_MONTH, 20);
        this.scadenza = scadenza.getTime();
    }

    public void setMotoreScelto(Motore motoreScelto) {
        if(motoreScelto == null){
            throw new IllegalArgumentException("Motore nullo inserito");
        }
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
        if(d == null){
            throw new IllegalArgumentException("Data nulla inserita");
        }
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
        if(valutazione<0){
            throw new IllegalArgumentException("Valutazione incorretta");
        }
        if(usata == null){
            throw new RuntimeException();
        }
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
        if(stato == null){
            throw new IllegalArgumentException("Stato nullo inserito");
        }
        this.stato = stato;
    }

    /**
     *   Aggiorna automaticamente lo stato del preventivo:
     *      -Se sono passati 20 giorni, e non è stato pagato il preventivo, viene assegnato lo stato SCADUTO
     *   Tale funzione sarà chiamata ogni qualvolta viene caricato il RegistroModel, tenendo aggiornati i dati
     * */
    public void updateStatoAutomatico(){
        if(stato == StatoPreventivo.FINALIZZATO && isScaduto()){
            setStato(StatoPreventivo.SCADUTO);
        }
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

    /**
     * Un preventivo è definito uguale ad un altro preventivo solo nel caso in cui:
     *      -L'auto che si vuole acquistare è uguale
     *      -L'auto usata che si vuole valutare è uguale
     *      -Il cliente che lo richiede è lo stesso
     *      -Gli optional scelti sono gli stessi
     *      -La sede di ritiro definita nel preventivo è la stessa
     * @param o oggetto con cui effettuare il confronto
     * @return
     */
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
        int diff = o.stato.compareTo(this.stato);
        if(diff != 0){
            return diff;
        }
        return this.data.compareTo(o.data);
    }
}
