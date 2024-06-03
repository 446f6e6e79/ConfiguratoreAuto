package org.example.configuratoreauto.Macchine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CatalogoTest{
    CatalogoModel c = CatalogoModel.getInstance();

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
        assertDoesNotThrow(() -> new AutoNuova(c.getUniqueId()));
        assertThrowsExactly(IllegalArgumentException.class, () -> new AutoNuova(c.getAllData().get(0).getId()));
    }
}