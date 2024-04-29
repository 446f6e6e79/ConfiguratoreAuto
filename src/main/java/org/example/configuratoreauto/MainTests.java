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
        catalogo.uploadData();


        System.out.println("Get auto by brand: brand = Porche" +catalogo.getAutoByBrand(Marca.Porsche));
        System.out.println("Get auto by brand: brand = BMW" +catalogo.getAutoByBrand(Marca.BMW));
        System.out.println("Get auto by brand: brand = Porche" +catalogo.(Marca.Porsche));


    }
}
