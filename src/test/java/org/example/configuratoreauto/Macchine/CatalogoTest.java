package org.example.configuratoreauto.Macchine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogoTest{
    CatalogoModel c = CatalogoModel.getInstance();

    @Test
    @DisplayName("Unique ID generator")
    void setValutazioneAutomatica(){
        HashSet<Integer> test = new HashSet<>();
        int nTests = 100000;
        for(int i = 0; i<nTests; i++){
            test.add(c.getUniqueId());
        }
        assertEquals(test.size(), nTests);
    }
}