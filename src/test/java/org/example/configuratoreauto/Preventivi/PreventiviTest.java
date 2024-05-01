package org.example.configuratoreauto.Preventivi;

import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.CatalogoModel;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

class PreventiviTest {
    RegistroModel r = RegistroModel.getInstance();
    UserModel u = UserModel.getInstance();
    AutoNuova exampleAuto = CatalogoModel.getInstance().getAllData().get(0);
    Sede sede = SediModel.getInstance().getAllData().get(0);

    Calendar c = Calendar.getInstance();

    @Test
    @DisplayName("Test sull'aggiornamento automatico dello stato")
    void addData(){
        u.validation("davidedona@gmail.com", "1234");
        Cliente c1 = (Cliente) u.getCurrentUser();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -40);
        Date d = c.getTime();
        Preventivo p = new Preventivo(null, exampleAuto, sede, c1, d);
        p.setValutazioneAutomatica();
        System.out.println(p.getStato());
    }

    @Test
    void getAllPreventivi() {
    }

    @Test
    void getPreventiviByBrand() {
    }

    @Test
    void getPreventiviByCliente() {
    }

    @Test
    void getPreventiviBySede() {
    }

    @Test
    void getPreventiviByStato() {
    }
}