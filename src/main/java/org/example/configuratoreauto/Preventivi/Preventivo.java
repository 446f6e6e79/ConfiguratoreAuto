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

    private final Date dataPreventivo;
    private Date dataConsegna;
    private Date dataScadenza;
    private StatoPreventivo stato;
    private AutoUsata usata;
    private AutoNuova acquisto;
    private ArrayList<Optional> optionalsSelezionati = new ArrayList<>();
    private Motore motoreScelto;
    private Sede sede;
    private Cliente cliente;

    private double valutazione;

    /**
     * Costruttore della classe preventivo, usato solamente per testing. Permette di creare un preventivo
     * in una data, passata come parametro
     */
    public Preventivo(AutoNuova acquisto, Sede sede, Cliente cliente, Date d, Motore motore, ArrayList optionalScelti){
        if(acquisto == null || sede == null || motore == null || cliente == null){
            throw new IllegalArgumentException("Errore nella creazione del preventivo");
        }
        this.dataPreventivo = d;
        this.stato = StatoPreventivo.RICHIESTO;
        this.acquisto = acquisto;
        this.sede = sede;
        this.cliente = cliente;
        this.motoreScelto = motore;
        if(optionalScelti != null){
            this.optionalsSelezionati.addAll(optionalScelti);
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
     * @throws IllegalArgumentException se si tenta di aggiungere un auto usata:
     *  <p>- a un preventivo già finalizzato
     *  <p>- a un preventivo con già un auto usata
     */
    public void setUsata(AutoUsata usata){
        if(stato == StatoPreventivo.FINALIZZATO || this.usata != null){
            throw new IllegalArgumentException("Il preventivo ha già un auto usata");
        }
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
        return acquisto.getSconto(dataPreventivo);
    }

    public String getScontoAutoFormatted(){
        return getPriceAsString(acquisto.getCostoTotale(optionalsSelezionati, dataPreventivo) - acquisto.getPrezzoNoSconto(optionalsSelezionati));
    }
    /**
    *  Calcola la data di consegna effettiva della macchina.
    *  La data è calcolata come:
    *     - data richiesta + 1 mese + (10 giorni * numeroOptional)
    * */
    private void setConsegna() {
        Calendar dataDiConsegna = Calendar.getInstance();
        dataDiConsegna.setTime(dataPreventivo);
        dataDiConsegna.add(Calendar.MONTH, 1);
        if(optionalsSelezionati != null){
            long count = optionalsSelezionati.stream()
                    .filter(optional -> !(optional.getCosto() == 0 && optional.getCategoria() == TipoOptional.Colore))
                    .count();
            dataDiConsegna.add(Calendar.DAY_OF_MONTH, 10 * (int) count);
        }
        this.dataConsegna = dataDiConsegna.getTime();
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
        scadenza.setTime(dataPreventivo);
        scadenza.add(Calendar.DAY_OF_MONTH, 20);
        this.dataScadenza = scadenza.getTime();
    }

    public Motore getMotoreScelto() {
        return motoreScelto;
    }

    /**
        Verifica che il preventivo non sia scaduto. Un preventivo è considerato scaduto dopo 20 giorni dalla finalizzazione
    */
    private boolean isScaduto(){
        return new Date().after(dataScadenza);
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
        return getDataAsString(this.dataPreventivo);
    }

    public String getDataConsegnaAsString(){
        return getDataAsString(this.dataConsegna);
    }

    public String getDataScadenzaAsString(){
        return getDataAsString(this.dataScadenza);
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
     * @throws IllegalArgumentException se la valutazione è < 0
     * @throws RuntimeException se valutiamo un preventivo, senza usato
    * */
    public void setValutazione(double valutazione) {
        if(valutazione<0){
            throw new IllegalArgumentException("Valutazione incorretta");
        }
        if(usata == null){
            throw new RuntimeException("Non si può effettuare una valutazione su tale preventivo");
        }
        this.valutazione = valutazione;
        setStato(StatoPreventivo.FINALIZZATO);
        setScadenza(new Date());
        setConsegna();
    }

    public ArrayList<Optional> getOptionalScelti() {
        return optionalsSelezionati;
    }

    public Sede getSede() {
        return sede;
    }

    public Date getDataPreventivo() {
        return dataPreventivo;
    }

    /**
     * Verifica se lo stato corrente risulta coerente con la modifica allo stato successivo
     * Se si effettua la modifica
     * @param stato Nuovo stato da impostare
     * @throws IllegalArgumentException se viene t
     */
    public void setStato(StatoPreventivo stato) {
        if (stato == null) {
            throw new IllegalArgumentException("Stato nullo inserito");
        }
        //UN preventivo può essere segnalato SCADUTO solo se prima era FINALIZZATO
        if(stato == StatoPreventivo.SCADUTO) {
            if (this.stato != StatoPreventivo.FINALIZZATO){
                throw new IllegalArgumentException("Nuovo stato non valido: SCADUTO può essere assegnato solo se lo stato precedente era FINALIZZATO");
            }
        }
        //UN preventivo può essere segnalato RITIRATO solo se prima era DISPOBILE_AL_RITIRO
        else if(stato == StatoPreventivo.RITIRATO){
            if(this.stato != StatoPreventivo.DISPONIBILE_AL_RITIRO){
                throw new IllegalArgumentException("Nuovo stato non valido: RITIRATO può essere assegnato solo se lo stato precedente era DISPONIBILE_AL_RITIRO");
            }
        }
        else if(stato == StatoPreventivo.RICHIESTO){
            if(this.stato != null) {
                throw new IllegalArgumentException("Nuovo stato non valido: RICHIESTO può essere assegnato solo se lo stato precedente era null");
            }
        }
        //Se non è nessuno dei casi precedenti, mi riconduco ad un caso base, verificabile attraverso il metodoOrdinal della Enumerazione
        else{
            if (stato.ordinal() != this.stato.ordinal() + 1) {
                throw new IllegalArgumentException("Stato non valido: la transizione non è consentita");
            }
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
        double tot = this.acquisto.getCostoTotale(this.optionalsSelezionati, this.dataPreventivo);
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
                Objects.equals(optionalsSelezionati, that.optionalsSelezionati) &&
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
        return this.dataPreventivo.compareTo(o.dataPreventivo);
    }
}
