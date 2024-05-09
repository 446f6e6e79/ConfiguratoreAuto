package org.example.configuratoreauto.Macchine;

import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.AutoUsata;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

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
        Assertions.assertTrue(test.size()==nTests);
    }
}