package org.example.configuratoreauto.Macchine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CatalogoTest{
    CatalogoModel c = CatalogoModel.getInstance();
    AutoNuova test = new AutoNuova();
    @Test
    @DisplayName("Unique ID generator")
    void setValutazioneAutomatica(){
        HashSet<Integer> test = new HashSet<>();
        int nTests = 10000;
        for(int i = 0; i < nTests; i++){
            int id = c.getUniqueId();
            c.addData(new AutoNuova(id));
            test.add(c.getUniqueId());
        }
        assertEquals(test.size(), nTests);
    }

    @Test
    @DisplayName("Check Existing id")
    void checkID(){
        AutoNuova a = c.getAllData().get(0);
        assertFalse(c.checkId(a.getId()));
    }

    @Test
    @DisplayName("New auto construtor")
    void createNewAuto(){
        assertDoesNotThrow(() -> test = new AutoNuova(c.getUniqueId()));
        assertThrowsExactly(IllegalArgumentException.class, () -> new AutoNuova(c.getAllData().get(0).getId()));
    }

    @Test
    @DisplayName("Set prezzo Base")
    void setBasePrice(){
        assertDoesNotThrow(() -> test.setCostoBase(10000));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setCostoBase(-10000));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setCostoBase(0));
    }

    @Test
    @DisplayName("Creazione dimensione")
    void setDimensione(){
        assertDoesNotThrow(() -> test.setDimensione(new Dimensione(10, 10, 10, 10, 10)));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Dimensione(-1, 10, 10,10,0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Dimensione(1, 0, 10,10,0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Dimensione(1, 10, 0,10,0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Dimensione(1, 10, 10,-10,0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Dimensione(1, 10, 10,10,-20));

    }

    @Test
    @DisplayName("Set Descrizione")
    void setDescrizione(){
        assertDoesNotThrow(() -> test.setDescrizione("Descrizione test"));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setDescrizione(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setDescrizione(null));
        assertEquals(test.getDescrizione(), "Descrizione test");
    }

    @Test
    @DisplayName("Set sconto per mese")
    void setScontoPerMese(){
        assertDoesNotThrow(() -> test.setScontoPerMese(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));

        //Creazione di un array con meno di 12 elementi
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setScontoPerMese(new int[]{1,2,4}));

        //Creazione di array con sconti negativi
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setScontoPerMese(new int[]{0, 1, 2, 3, 4, 5, -6, 7, 8, 9, 10, 11}));

        //Array con sconti che eccedono il 100%
        assertThrowsExactly(IllegalArgumentException.class, () -> test.setScontoPerMese(new int[]{0, 101, 2, 3, 4, 5, -6, 7, 8, 9, 10, 11}));
    }

    @Test
    @DisplayName("Set Motori")
    void setMotoriDisponibili(){
        assertThrowsExactly(IllegalArgumentException.class, () -> test.addMotore(new Motore("", Alimentazione.ELETTRICA, 10, 10, 10)));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.addMotore(new Motore("TEST", Alimentazione.ELETTRICA, 10, 10, 10)));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.addMotore(new Motore("TEST", Alimentazione.ELETTRICA, -10, 0, 10)));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.addMotore(new Motore("TEST", Alimentazione.ELETTRICA, 10, 0, -10)));
        assertThrowsExactly(IllegalArgumentException.class, () -> test.addMotore(new Motore("TEST", Alimentazione.BENZINA, 10, -20, -10)));
        assertDoesNotThrow(() -> test.addMotore(new Motore("TEST", Alimentazione.ELETTRICA, 10, 0, 10)));
    }

    @Test
    @DisplayName("set Optional")
    void setOptionalDisponibili(){
        int prezzoTest = 1000;
        assertDoesNotThrow(() -> test.addOptional( new Optional(TipoOptional.Vetri, "Vetri oscurati", prezzoTest)));
        assertEquals(prezzoTest, test.getOptionalDisponibili().get(0).getCosto());
        assertDoesNotThrow(() -> test.addOptional( new Optional(TipoOptional.Vetri, "Vetri oscurati", 2000)));

        //Controllo che il secondo addOptional abbia sovrascritto il primo optional
        assertEquals(1, test.getOptionalDisponibili().size());
        assertNotEquals(prezzoTest, test.getOptionalDisponibili().get(0).getCosto());
        assertThrowsExactly(IllegalArgumentException.class, () -> new Optional(TipoOptional.Vetri, "", 10));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Optional(TipoOptional.Vetri, "TEST", -1000));
    }

}