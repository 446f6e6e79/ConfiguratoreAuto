package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.Preventivi.Preventivo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class AutoNuova extends Auto implements Serializable {
    //private final int id;
    private int id;
    private String descrizione;
    private Dimensione dimensione;
    private double costoBase;
    //Array di 12 double, rappresentano ogni mese lo scontoApplicato
    private double [] scontoPerMese;
    private ArrayList<Motore> motoriDisponibili = new ArrayList<>();
    private TreeSet<Optional> optionalDisponibili = new TreeSet<>();

    public AutoNuova(int id, Marca marca, String modello, Dimensione dimensione, String descrizione, double costoBase, double [] scontoPerMese){
        super(marca, modello);
        this.id = id;
        this.dimensione = dimensione;
        this.descrizione = descrizione;
        this.costoBase = costoBase;
        this.scontoPerMese = scontoPerMese;
        this.motoriDisponibili = new ArrayList<>();
        this.optionalDisponibili = new TreeSet<>();
    }

    public AutoNuova(int id){
        this.id = id;
    }

    public AutoNuova(){}

    //Metodo GET per il campoID
    public int getId() {
        return id;
    }

    //GET e SET costoBase
    public double getCostoBase() {
        return costoBase;
    }
    public void setCostoBase(double costoBase) {
        this.costoBase = costoBase;
    }

    //Get e SET per il campo dimensione
    public Dimensione getDimensione() {
        return dimensione;
    }
    public void setDimensione(Dimensione dimensione) {
        this.dimensione = dimensione;
    }

    //GET e SET descrizione
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    //GET e SET Sconto per mese
    public double[] getScontoPerMese() {
        return scontoPerMese;
    }
    public void setScontoPerMese(double[] scontoPerMese) {
        this.scontoPerMese = scontoPerMese;
    }

    //GET e SET motori disponibili
    public ArrayList<Motore> getMotoriDisponibili() {
        return motoriDisponibili;
    }
    public void setMotoriDisponibili(ArrayList<Motore> motoriDisponibili){
        this.motoriDisponibili = motoriDisponibili;
    }

    //GET E SET OPTIONAL
    public TreeSet<Optional> getOptionalDisponibili() {
        return optionalDisponibili;
    }
    public void setOptionalDisponibili(TreeSet<Optional> optionalDisponibili){
        this.optionalDisponibili = optionalDisponibili;
    }

    /**
     * Aggiunge un nuovo optional a quelli dipsonibili.
     * Se tale optional era già presente, lo sostituisce con quello nuovo
     * @param optional optioanl da aggiungere alla lista
     */
    public void addOptional(Optional optional){
        //Provo a rimuovere l'optional, se già presente
        optionalDisponibili.remove(optional);
        //Aggiungo l'optional
        optionalDisponibili.add(optional);
    }

    /**
     *
     * @param category Categoria di optional
     * @return ritorna tutti gli optional di tipo category, dispobili per tale modello
     */
    public ArrayList<Optional> getOptionalByCategory(TipoOptional category){
        return optionalDisponibili.stream().filter(optional -> optional.getCategoria().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *
     * @return Restituisce un arrayList contenente tutti i colori aggiunti per tale macchina
     */
    public ArrayList<String> getUsedColors(){
        HashSet<String> colors = new HashSet<>();
        for(Optional colore: getOptionalByCategory(TipoOptional.colore)){
            colors.add(colore.getDescrizione());
        }
        return new ArrayList<>(colors);
    }

    public double getSconto(Date data){
        return this.getScontoPerMese()[data.getMonth()];
    }

    /*
    *   Due auto sono considerate uguali se condividono lo stesso id
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof AutoNuova other &&
                this.id == other.id;
    }

    public void addMotore(Motore motore){
        motoriDisponibili.add(motore);
    }

    /**
     * Calcola il costo Totale di un auto:
     * @param chosenOtionals lista di optional SELEZIONATI
     * @param data data in cui è calcolato il prezzo:
     *             - PREVENTIVO: data = data richiesta preventivo
     *             - Modifica auto: data = data attuale
     * @return double costo totale, calcolato come:
     *      costo di base + costo Optional (- valutazione usato) - sconto
     */
    public double getCostoTotale(ArrayList<Optional> chosenOtionals, Date data){
        double tot = this.costoBase;
        for(Optional optional : chosenOtionals){
            tot += optional.getCosto();
        }
        tot -= (tot*getSconto(data) / 100);
        return tot;
    }

    public String getBasePriceAsString(){
        return Preventivo.getPriceAsString(costoBase);
    }
}
