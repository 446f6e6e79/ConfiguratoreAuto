package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Utenti.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PreventiviTest{

    Preventivo test;
    CatalogoModel c = CatalogoModel.getInstance();
    AutoNuova auto = new AutoNuova(c.getUniqueId());
    Sede sede = SediModel.getInstance().getAllData().get(0);
    Motore motore = new Motore("Test", Alimentazione.ELETTRICA,10,0,10);
    Cliente cliente = new Cliente("tester@gmail.com","1234","Test","Test");
    AutoUsata usata = new AutoUsata(Marca.Ferrari, "test", "TT268PN",0);

    @Test
    @DisplayName("Aggiornamento stato automatico")
    void setValutazioneAutomatica(){
        //Creiamo un preventivo con data + 20 giorni fa e vediamo se, una volta aggiornato, risulta scaduto
        Date date = new Date();
        date.setMonth(date.getMonth()-1);//Creo una data di un mese indietro rispetto alla data attuale
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, date, motore, null));
        assertDoesNotThrow(() -> test.setUsata(null));
        assertDoesNotThrow(() ->  test.updateStatoAutomatico());
        assertEquals(test.getStato(), StatoPreventivo.SCADUTO);
        //Verifico se setValutazione aggiorni correttamente i dati
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, new Date(), motore, null));
        assertDoesNotThrow(()->test.setUsata(usata));
        assertEquals(test.getStato(), StatoPreventivo.RICHIESTO);
        assertDoesNotThrow(() -> test.setValutazione(1000));
        assertEquals(test.getStato(), StatoPreventivo.FINALIZZATO);
        assertDoesNotThrow(() -> test.setStato(StatoPreventivo.PAGATO));
        assertEquals(test.getStato(), StatoPreventivo.PAGATO);
        //Gestione errori
        assertThrowsExactly(IllegalArgumentException.class, ()->test.setStato(null));
        assertThrowsExactly(IllegalArgumentException.class, ()->test.setValutazione(-30));
    }

    @Test
    @DisplayName("Creazione preventivi")
    void createPreventivo(){

        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, motore, null));

        /* To Do:
            TEST su preventivi con dati null, dati non validi, clienti di tipo non cliente...))
            Test sull'aggiunta di durata al preventivo se optional scelti
            Test sulla non aggiunta di durata al preventivo se 0 optional scelti
         */


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
        AutoUsata usata = new AutoUsata(Marca.Ferrari, "test", "TT268PN",0);
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setUsata(usata));

        //Creiamo un nuovo preventivo e aggiungiamo un auto
        test = new Preventivo(auto, sede, cliente, motore, null);
        test.setUsata(usata);
        assertEquals(test.getStato(), StatoPreventivo.RICHIESTO);
        assertThrowsExactly(IllegalArgumentException.class, ()-> test.getDataScadenzaAsString());
        assertThrowsExactly(IllegalArgumentException.class, ()-> test.getDataConsegnaAsString());
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setUsata(usata));
    }
}