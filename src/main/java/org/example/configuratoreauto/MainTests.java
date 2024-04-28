package org.example.configuratoreauto;

import org.example.configuratoreauto.Utenti.*;

public class MainTests {
    public static void main(String[] args) {
        UserModel u1 = (UserModel) UserModel.getInstance();

        u1.addData(new Cliente("guest", "guest", "Prova", "Prova"));
        u1.addData(new Cliente("davidedona@gmail.com", "1234", "Davide", "Donà"));
        u1.addData(new Cliente("andreablushi@gmail.com", "1234", "Andrea", "Blushi"));
        u1.addData(new Cliente("ojogvictor@gmail.com", "1234", "Victor", "Ojog"));
        u1.addData(new Segretario("provaSegretario@gmail.com", "1234"));
        u1.addData(new Impiegato("provaImpiegato@gmail.com", "1234"));
        u1.uploadData();

        System.out.println("TESTING FOR THE USER MODEL:" +
                "\n\tTRUE: user logged in / registered" +
                "\n\tFALSE:user not logged in/ not registered\n");

        boolean r;
        //test login
        r = u1.login("", "");
        System.out.println("TEST: login with empty string: " + r);

        //r = u1.login(null, null);
        //System.out.println("TEST: login with null:" + r);

        r = u1.login("guest", "guest");
        System.out.println("TEST: login with existing user: " + r);

        r = (u1.getCurrentUser() instanceof Cliente);
        System.out.println("TEST: user logged in is of type Cliente: "+r);

        r = u1.login("guest", "Guest");
        System.out.println("TEST: login with existing mail, but wrong password: " + r);

        r = u1.addData(new Cliente("provaSegretario@gmail.com", "4321", "", ""));
        System.out.println("TEST: registering a new user, with elready existing mail: " + r);

    }
}
