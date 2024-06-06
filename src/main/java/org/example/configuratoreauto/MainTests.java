package org.example.configuratoreauto;

import org.example.configuratoreauto.Macchine.*;
import org.example.configuratoreauto.Preventivi.Preventivo;
import org.example.configuratoreauto.Preventivi.RegistroModel;
import org.example.configuratoreauto.Preventivi.Sede;
import org.example.configuratoreauto.Preventivi.SediModel;
import org.example.configuratoreauto.Utenti.*;

import java.util.ArrayList;
import java.util.Date;


public class MainTests {

    public static void main(String[] args) {
        UserModel u1 = UserModel.getInstance();
        RegistroModel r = RegistroModel.getInstance();
        u1.validation("davide@gmail.com", "1234");
        Sede s = SediModel.getInstance().data.get(0);
        System.out.println(s);
        AutoNuova a = CatalogoModel.getInstance().data.get(1);

        Date date = new Date();
        date.setMonth(date.getMonth()-1);

        Preventivo p = new Preventivo(a, s, (Cliente) u1.getCurrentUser(), date, a.getMotoriDisponibili().get(0), new ArrayList());

        p.setUsata(null);
        r.addData(p);
        r.uploadData();
        /*
        UserModel u1 = (UserModel) UserModel.getInstance();
        RegistroModel r = RegistroModel.getInstance();
        RegistroModel r = RegistroModel.getInstance();

        // Popolare i dati utente
        u1.registraCliente(new Cliente("davide@gmail.com", "1234", "Davide", "Donà"));
        u1.validation("davide@gmail.com", "1234");
        u1.registraCliente(new Cliente("andrea@gmail.com", "1234", "Andrea", "Blushi"));
        u1.registraCliente(new Cliente("victor@gmail.com", "1234", "Victor", "Ojog"));
        u1.addData(new Segretario("segretario@gmail.com", "1234"));
        u1.addData(new Impiegato("impiegato@gmail.com", "1234"));
        u1.uploadData();

        // Popolare i dati delle sedi
        sedi.addData(new Sede("AutoVerona", "Str. le Grazie, 15 - 37134 Verona VR"));
        sedi.addData(new Sede("AutoExpert", "via Monte Cellarino, 12 - Roma RM"));
        sedi.addData(new Sede("DriveNow", "Via Antonio Giacomini, 16 - 50132 Firenze"));
        sedi.addData(new Sede("MotorWorld", "Via della Moscova, 10 - 20121 Milano MI"));
        sedi.addData(new Sede("CarNapoli", "Via Toledo, 156 - 80134 Napoli NA"));
        sedi.uploadData();


        //Aggiungere più preventivi per Davide Dona






        // Creazione di oggetti esempio necessari per il Preventivo


        // Creazione del Preventivo

        // Stampa i dettagli del Preventivo


        //Test for the CATALOGO
        Dimensione dim911 = new Dimensione(1.90, 1.32, 4.57, 1525, 0);
        Motore motore911 = new Motore("V8 TURBO", Alimentazione.BENZINA, 386, 3966,13.4);
        String descrizione911 = "Macchina molto bella";
        int [] scontoPerMese = {0, 10, 10, 0, 10, 20, 7, 10, 0, 0 , 0, 0};
        AutoNuova a1 = new AutoNuova(10, Marca.Porsche, "911 GT3 RS", dim911, descrizione911, 258536, scontoPerMese);
        a1.addMotore(motore911);
        catalogo.addData(a1);

        Dimensione dimFerrari = new Dimensione(1.85, 1.30, 4.60, 1550, 0);
        Motore motoreFerrari = new Motore("MuitJet", Alimentazione.BENZINA, 400, 4000, 14.0);
        String descrizioneFerrari = "Auto Ferrari molto potente";
        int[] scontoPerMeseFerrari = {0, 5, 8, 5, 5, 10, 12, 10, 5, 0, 0, 0};
        AutoNuova ferrari = new AutoNuova(20, Marca.Ferrari, "F8 Tributo", dimFerrari, descrizioneFerrari, 280000, scontoPerMeseFerrari);
        ferrari.addMotore(motoreFerrari);
        catalogo.addData(ferrari);

        Dimensione dimLamborghini = new Dimensione(1.92, 1.38, 4.80, 1650, 0);
        Motore motoreLamborghini = new Motore("V18", Alimentazione.BENZINA, 420, 6000, 15.0);
        String descrizioneLamborghini = "Auto Lamborghini ad alte prestazioni";
        int[] scontoPerMeseLamborghini = {0, 8, 8, 5, 5, 10, 15, 15, 8, 0, 0, 0};
        AutoNuova lamborghini = new AutoNuova(30, Marca.Lamborghini, "Aventador SVJ", dimLamborghini, descrizioneLamborghini, 550000, scontoPerMeseLamborghini);
        lamborghini.addMotore(motoreLamborghini);
        catalogo.addData(lamborghini);

        Dimensione dimAudi = new Dimensione(1.75, 1.30, 4.65, 1550, 0);
        Motore motoreAudi = new Motore("VolksWagneMotor", Alimentazione.BENZINA, 350, 4000, 13.5);
        String descrizioneAudi = "Auto Audi di lusso e prestazioni elevate";
        int[] scontoPerMeseAudi = {0, 8, 8, 5, 5, 10, 12, 10, 5, 0, 0, 0};
        AutoNuova audi = new AutoNuova(40, Marca.Audi, "R8 V10 Plus", dimAudi, descrizioneAudi, 180000, scontoPerMeseAudi);
        audi.addMotore(motoreAudi);
        catalogo.addData(audi);

        Dimensione dimMercedes = new Dimensione(1.78, 1.30, 4.70, 1600, 0);
        Motore motoreMercedes = new Motore("Renault type A", Alimentazione.BENZINA, 400, 4500, 14.0);
        String descrizioneMercedes = "Auto Mercedes-Benz ad alte prestazioni";
        int[] scontoPerMeseMercedes = {0, 8, 8, 5, 5, 10, 12, 10, 5, 0, 0, 0};
        AutoNuova mercedes = new AutoNuova(50, Marca.Mercedes, "AMG GT R", dimMercedes, descrizioneMercedes, 190000, scontoPerMeseMercedes);
        mercedes.addMotore(motoreMercedes);
        catalogo.addData(mercedes);

        Dimensione dimTesla = new Dimensione(1.75, 1.30, 4.70, 1700, 0);
        Motore motoreTesla = new Motore("ElonMusk", Alimentazione.ELETTRICA, 700, 0, 15.0);
        String descrizioneTesla = "Auto Tesla ad alte prestazioni";
        int[] scontoPerMeseTesla = {0, 10, 12, 8, 5, 5, 10, 12, 8, 0, 0, 0};
        AutoNuova tesla = new AutoNuova(60, Marca.Tesla, "Model S Plaid", dimTesla, descrizioneTesla, 125000, scontoPerMeseTesla);
        tesla.addMotore(motoreTesla);
        catalogo.addData(tesla);

        Dimensione dimFord = new Dimensione(1.75, 1.30, 4.70, 1500, 0);
        Motore motoreFord = new Motore("Siesta",Alimentazione.BENZINA, 300, 4000, 12.0);
        String descrizioneFord = "Auto Ford dal design sportivo";
        int[] scontoPerMeseFord = {0, 8, 8, 5, 5, 10, 10, 10, 5, 0, 0, 0};
        AutoNuova ford = new AutoNuova(70, Marca.Ford, "Mustang GT", dimFord, descrizioneFord, 55000, scontoPerMeseFord);
        ford.addMotore(motoreFord);
        catalogo.addData(ford);

        dimFerrari = new Dimensione(2.02, 1.58, 4.97, 2033, 0);
        motoreFerrari = new Motore("F140IA V12", Alimentazione.BENZINA, 715, 8000, 6.5);
        descrizioneFerrari = "Ferrari Purosangue con prestazioni eccezionali";
        scontoPerMeseFerrari = new int[]{0, 5, 5, 5, 5, 10, 10, 10, 5, 0, 0, 0};
        ferrari = new AutoNuova(85, Marca.Ferrari, "Purosangue", dimFerrari, descrizioneFerrari, 350000, scontoPerMeseFerrari);
        ferrari.addMotore(motoreFerrari);
        catalogo.addData(ferrari);


        catalogo.uploadData();
        //
        System.out.println("Get auto by brand: brand = Porche " +CatalogoModel.filterAutoByBrand(Marca.Porsche, catalogo.getAllData()));
        System.out.println("Get auto by Alimentazione: Elettrico" +CatalogoModel.filterAutoByAlimentazione(Alimentazione.ELETTRICA, catalogo.getAllData()));
        System.out.println("Get all auto" +catalogo.getAllData());
    //
        for (int i = 0; i < 3; i++) {
            Preventivo p = new Preventivo(catalogo.getAllData().get(i), sedi.getAllData().get(0), (Cliente) u1.getCurrentUser(),null, null);
            p.setMotoreScelto(catalogo.getAllData().get(i).getMotoriDisponibili().get(0)); // Imposta il primo motore disponibile per l'auto
            p.setUsata(null);
            r.addData(p);
        }

        r.uploadData();
        */
    }
}
