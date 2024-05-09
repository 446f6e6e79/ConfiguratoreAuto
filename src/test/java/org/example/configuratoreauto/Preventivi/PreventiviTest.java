package org.example.configuratoreauto.Preventivi;

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

class PreventiviTest{
    RegistroModel r = RegistroModel.getInstance();
    UserModel u = UserModel.getInstance();
    AutoNuova exampleAuto = CatalogoModel.getInstance().getAllData().get(0);
    Sede sede = SediModel.getInstance().getAllData().get(0);
    Calendar c = Calendar.getInstance();

    @Test
    @DisplayName("Aggiornamento Automatico STATO")
    void setValutazioneAutomatica(){

        u.validation("davidedona@gmail.com", "1234");
        Cliente c1 = (Cliente) u.getCurrentUser();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -100);
        Date d = c.getTime();;
        AutoUsata aUsata = new AutoUsata(Marca.Porsche, "prova", exampleAuto.getDimensione(), exampleAuto.getMotore(), "12345", 20000);
        Preventivo p = new Preventivo(null, exampleAuto, sede, c1, d);
        p.setStato(StatoPreventivo.FINALIZZATO);
        Assertions.assertEquals(StatoPreventivo.FINALIZZATO, p.getStato());

        //Una volta aggiornata automaticamente lo stato, il preventivo dovrebbe risultare SCADUTO
        p.updateStatoAutomatico();
        Assertions.assertEquals(StatoPreventivo.SCADUTO, p.getStato());
    }

    @Test
    @DisplayName("Valutazione macchina usata")
    void setValutazione() {
        u.validation("davidedona@gmail.com", "1234");
        Cliente c1 = (Cliente) u.getCurrentUser();
        AutoUsata u = new AutoUsata(Marca.Audi, "Audi a1", exampleAuto.getDimensione(), exampleAuto.getMotore(), "1234", 10);
        Preventivo pUsata = new Preventivo(u, exampleAuto, sede, c1);
        pUsata.updateStatoAutomatico();

        //Il preventivo, avendo un auto usata, avrà come stato RICHIESTO
        Assertions.assertEquals(StatoPreventivo.RICHIESTO, pUsata.getStato());

        double firstPrice = pUsata.getCostoTotale();
        /*
        *   Una volta impostata la valutazione verico che effetivamente:
        *       - lo stato del preventivo diventi FINALIZZATO
        *       - dal costo totale del preventivo sia sottratta l'intera valutazione
        * */
        pUsata.setValutazione(3500);
        Assertions.assertEquals(3500, firstPrice - pUsata.getCostoTotale());
        Assertions.assertEquals(StatoPreventivo.FINALIZZATO, pUsata.getStato());
    }

    @Test
    @DisplayName("Filtro Preventivi per Brand")
    void getPreventiviByBrand() {
        ArrayList<Preventivo> preventivi = r.getPreventiviByBrand(Marca.Porsche);
        for(Preventivo p: preventivi){
            Assertions.assertEquals(Marca.Porsche, p.getAcquisto().getMarca());
        }
    }

    @Test
    @DisplayName("Filtro Preventivi per Sede")
    void getPreventiviBySede(){
        ArrayList<Preventivo> preventivi = r.getPreventiviBySede(sede);
        for(Preventivo p: preventivi){
            Assertions.assertEquals(sede, p.getSede());
        }
    }
}