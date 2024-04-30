package org.example.configuratoreauto;

import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Utenti.*;

import java.util.ArrayList;

public class MainTests {
    public static void main(String[] args) {
        UserModel u1 = (UserModel) UserModel.getInstance();

        u1.registraCliente(new Cliente("guest", "guest", "Prova", "Prova"));
        u1.registraCliente(new Cliente("davidedona@gmail.com", "1234", "Davide", "Donà"));
        u1.registraCliente(new Cliente("andreablushi@gmail.com", "1234", "Andrea", "Blushi"));
        u1.registraCliente(new Cliente("ojogvictor@gmail.com", "1234", "Victor", "Ojog"));
        u1.addData(new Segretario("provaSegretario@gmail.com", "1234"));
        u1.addData(new Impiegato("provaImpiegato@gmail.com", "1234"));
        u1.uploadData();

        System.out.println("TESTING FOR THE USER MODEL:" +
                "\n\tTRUE: user logged in / registered" +
                "\n\tFALSE:user not logged in/ not registered\n");

        boolean r;
        //test login

        r = u1.validation("", "");
        System.out.println("TEST: login with empty string: " + r +" -EXPECTED FALSE");

        //r = u1.login(null, null);
        //System.out.println("TEST: login with null:" + r);

        r = u1.validation("guest", "guest");
        System.out.println("TEST: login with existing user: " + r +" -EXPECTED FALSE");


        r = (u1.getCurrentUser() instanceof Cliente);
        System.out.println("TEST: user logged in is of type Cliente: "+r);

        r = u1.validation("guest", "Guest");
        System.out.println("TEST: login with existing mail, but wrong password: " + r);

        r = u1.addData(new Cliente("provaSegretario@gmail.com", "4321", "", ""));
        System.out.println("TEST: registering a new user, with already existing mail: " + r);



        /*Test for the CATALOGO*/
        CatalogoModel catalogo = CatalogoModel.getInstance();
        Dimensione dim911 = new Dimensione(1.90, 1.32, 4.57, 1525, 0);
        Motore motore911 = new Motore(Alimentazione.BENZINA, 386, 3966,13.4);
        String descrizione911 = "Macchina molto bella";
        double [] scontoPerMese = {0, 10, 10, 0, 10, 20, 7, 10, 0, 0 , 0, 0};
        AutoNuova a1 = new AutoNuova(Marca.Porsche, "911 GT3 RS", dim911, motore911, descrizione911, 258536, scontoPerMese);

        catalogo.addData(a1);
        Dimensione dimFerrari = new Dimensione(1.85, 1.30, 4.60, 1550, 0);
        Motore motoreFerrari = new Motore(Alimentazione.BENZINA, 400, 4000, 14.0);
        String descrizioneFerrari = "Auto Ferrari molto potente";
        double[] scontoPerMeseFerrari = {0, 5, 8, 5, 5, 10, 12, 10, 5, 0, 0, 0};
        AutoNuova ferrari = new AutoNuova(Marca.Ferrari, "F8 Tributo", dimFerrari, motoreFerrari, descrizioneFerrari, 280000, scontoPerMeseFerrari);
        catalogo.addData(ferrari);

        Dimensione dimLamborghini = new Dimensione(1.92, 1.38, 4.80, 1650, 0);
        Motore motoreLamborghini = new Motore(Alimentazione.BENZINA, 420, 6000, 15.0);
        String descrizioneLamborghini = "Auto Lamborghini ad alte prestazioni";
        double[] scontoPerMeseLamborghini = {0, 8, 8, 5, 5, 10, 15, 15, 8, 0, 0, 0};
        AutoNuova lamborghini = new AutoNuova(Marca.Lamborghini, "Aventador SVJ", dimLamborghini, motoreLamborghini, descrizioneLamborghini, 550000, scontoPerMeseLamborghini);
        catalogo.addData(lamborghini);

        Dimensione dimAudi = new Dimensione(1.75, 1.30, 4.65, 1550, 0);
        Motore motoreAudi = new Motore(Alimentazione.BENZINA, 350, 4000, 13.5);
        String descrizioneAudi = "Auto Audi di lusso e prestazioni elevate";
        double[] scontoPerMeseAudi = {0, 8, 8, 5, 5, 10, 12, 10, 5, 0, 0, 0};
        AutoNuova audi = new AutoNuova(Marca.Audi, "R8 V10 Plus", dimAudi, motoreAudi, descrizioneAudi, 180000, scontoPerMeseAudi);
        catalogo.addData(audi);

        Dimensione dimMercedes = new Dimensione(1.78, 1.30, 4.70, 1600, 0);
        Motore motoreMercedes = new Motore(Alimentazione.BENZINA, 400, 4500, 14.0);
        String descrizioneMercedes = "Auto Mercedes-Benz ad alte prestazioni";
        double[] scontoPerMeseMercedes = {0, 8, 8, 5, 5, 10, 12, 10, 5, 0, 0, 0};
        AutoNuova mercedes = new AutoNuova(Marca.Mercedes, "AMG GT R", dimMercedes, motoreMercedes, descrizioneMercedes, 190000, scontoPerMeseMercedes);
        catalogo.addData(mercedes);

        Dimensione dimTesla = new Dimensione(1.75, 1.30, 4.70, 1700, 0);
        Motore motoreTesla = new Motore(Alimentazione.ELETTRICA, 700, 0, 15.0);
        String descrizioneTesla = "Auto Tesla ad alte prestazioni";
        double[] scontoPerMeseTesla = {0, 10, 12, 8, 5, 5, 10, 12, 8, 0, 0, 0};
        AutoNuova tesla = new AutoNuova(Marca.Tesla, "Model S Plaid", dimTesla, motoreTesla, descrizioneTesla, 125000, scontoPerMeseTesla);
        catalogo.addData(tesla);

        Dimensione dimFord = new Dimensione(1.75, 1.30, 4.70, 1500, 0);
        Motore motoreFord = new Motore(Alimentazione.BENZINA, 300, 4000, 12.0);
        String descrizioneFord = "Auto Ford dal design sportivo";
        double[] scontoPerMeseFord = {0, 8, 8, 5, 5, 10, 10, 10, 5, 0, 0, 0};
        AutoNuova ford = new AutoNuova(Marca.Ford, "Mustang GT", dimFord, motoreFord, descrizioneFord, 55000, scontoPerMeseFord);
        catalogo.addData(ford);



        System.out.println("Get auto by brand: brand = Porche " +catalogo.getAutoByBrand(Marca.Porsche));
        System.out.println("Get all auto" +catalogo.getAllAuto());


    }
}
