package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;
import org.example.configuratoreauto.Utenti.Persona;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PreventiviTest{

    Preventivo test;
    Preventivo tester2;
    CatalogoModel c = CatalogoModel.getInstance();
    AutoNuova auto = new AutoNuova(c.getUniqueId());
    Sede sede = SediModel.getInstance().getAllData().get(0);
    Motore motore = new Motore("Test", Alimentazione.ELETTRICA,10,0,10);
    Cliente cliente = new Cliente("tester@gmail.com","1234","Test","Test");
    AutoUsata usata = new AutoUsata(Marca.Ferrari, "test", "TT268PN",0);

    @Test
    @DisplayName("Aggiornamento stato automatico")
    void updateStatoAutomatico(){
        //Creiamo un preventivo con data + 20 giorni fa e vediamo se, una volta aggiornato, risulta scaduto
        Date date = new Date();
        date.setMonth(date.getMonth()-1);//Creo una data di un mese indietro rispetto alla data attuale
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, date, motore, null));
        assertDoesNotThrow(() -> test.setUsata(null));
        assertDoesNotThrow(() ->  test.updateStatoAutomatico());
        assertEquals(test.getStato(), StatoPreventivo.SCADUTO);


//        assertDoesNotThrow(() -> test.setStato(StatoPreventivo.PAGATO));
//        assertEquals(test.getStato(), StatoPreventivo.PAGATO);
//        //Gestione errori
//        assertThrowsExactly(IllegalArgumentException.class, ()->test.setStato(null));
//        assertThrowsExactly(IllegalArgumentException.class, ()->test.setValutazione(-30));
    }

    @Test
    @DisplayName("Creazione preventivi")
    void createPreventivo(){
        ArrayList<Optional> optionalsCost = new ArrayList<>();
        ArrayList<Optional> optionalsZero = new ArrayList<>();
        for(int i=0; i<20; i++){
            Optional optional = new Optional(TipoOptional.Vetri, "test"+i, 0+i*10);
            Optional optional2 = new Optional(TipoOptional.Cerchi, "test"+i, 0);
            optionalsCost.add(optional);
            optionalsZero.add(optional2);
        }
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, motore, null));
        //Inconsistenza dei dati
        assertThrowsExactly(IllegalArgumentException.class, () -> test = new Preventivo(null, sede, cliente, motore, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> test = new Preventivo(auto, sede,null, motore, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> test = new Preventivo(auto, null,cliente, motore, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> test = new Preventivo(auto, sede,cliente, null, null));
        //Preventivo senza optional
        assertDoesNotThrow(() -> tester2 = new Preventivo(auto, sede, cliente, motore, null));
        assertDoesNotThrow(() -> tester2.setUsata(null));

        //Preventivo con optional di costo zero
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, motore, optionalsZero));
        assertDoesNotThrow(() -> test.setUsata(null));
        //Dovrebbero avere la stessa data di consegna
        assertEquals(test.getDataConsegnaAsString(), tester2.getDataConsegnaAsString());
        //Preventivi con optional con un costo
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, motore, optionalsCost));
        assertDoesNotThrow(() -> test.setUsata(null));
        //Il preventivo senza dovrebbere avere una data di consegna diversa rispetto al preventivo con optional di valore
        assertNotEquals(test.getDataConsegnaAsString(), tester2.getDataConsegnaAsString());
    }

    @Test
    @DisplayName("Setting autoUsata")
    void settingAutoUsata(){
        test = new Preventivo(auto, sede, cliente, motore, null);

        //Creiamo un preventivo senza autoUsata
        assertThrowsExactly(IllegalArgumentException.class, ()-> test.getDataScadenzaAsString());
        assertThrowsExactly(IllegalArgumentException.class, ()-> test.getDataConsegnaAsString());
        assertDoesNotThrow(() -> test.setUsata(null));

        //Verifichiamo che, una volta settata l'auto usata a null, il preventivo risulta finalizzato
        assertEquals(test.getStato(), StatoPreventivo.FINALIZZATO);
        assertNotNull(test.getDataScadenzaAsString());
        assertNotNull(test.getDataConsegnaAsString());

        //Aggiungiamo una macchina USATA AD UN PREVENTIVO GIA' FINALIZZATO
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setUsata(usata));

        //Creiamo un nuovo preventivo e aggiungiamo un auto usata
        test = new Preventivo(auto, sede, cliente, motore, null);
        test.setUsata(usata);
        assertEquals(test.getStato(), StatoPreventivo.RICHIESTO);
        assertThrowsExactly(IllegalArgumentException.class, ()-> test.getDataScadenzaAsString());
        assertThrowsExactly(IllegalArgumentException.class, ()-> test.getDataConsegnaAsString());
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setUsata(usata));
    }
    @Test
    @DisplayName("Valutazione autoUsata")
    void setValutazioneAutoUsata(){
        AutoUsata usata = new AutoUsata(Marca.BMW, "TEST", "GG018VL", 10);
        test = new Preventivo(auto, sede, cliente, motore, null);
        test.setUsata(null);
        //Valutazione di un preventivo senza autoUsata
        assertThrowsExactly(RuntimeException.class, () -> test.setValutazione(1000));

        test = new Preventivo(auto, sede, cliente, motore, null);
        test.setUsata(usata);
        //Valutazione < 0
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setValutazione(-1000));

        //Imposto una valutazione corretta e verifico l'aggiornamento dello stato
        assertDoesNotThrow(() -> test.setValutazione(1000));
        assertEquals(test.getStato(), StatoPreventivo.FINALIZZATO);
        test.setStato(StatoPreventivo.RICHIESTO);

    }

    @Test
    @DisplayName("Creazione autoUsata")
    void createAutoUsata(){

        //Creazione auto usata valida
        //Verifica di autoUsata già inserita
        //Verifica autoUsata con campi non validi
    }

}