package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Utenti.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class PreventiviTest{

    Preventivo test;
    CatalogoModel c = CatalogoModel.getInstance();
    RegistroModel r = RegistroModel.getInstance();
    AutoNuova auto = new AutoNuova(c.getUniqueId());
    Sede sede = new Sede("Test", "Via Test");
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
    @DisplayName("Creazione preventivi e setting usata")
    void createPreventivo(){
        //Creiamo un preventivo senza auto usata, uno con
        Date date = new Date();
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, date, motore, null)); //Costruttore 1
        assertDoesNotThrow(() -> test = new Preventivo(auto, sede, cliente, motore, null)); //Costruttore 2
        assertDoesNotThrow(() -> test.setUsata(null));
        assertThrowsExactly(IllegalArgumentException.class, ()->test.setMotoreScelto(null));
        assertThrowsExactly(RuntimeException.class, ()->test.setValutazione(300));
        assertDoesNotThrow(() -> test.setUsata(usata));

    }
}