package org.example.configuratoreauto.Utenti;

import java.io.Serializable;

public class Persona implements Serializable{
    //Email della persona, utilizzata come credenziale per il login
    private String email;
    //Password, utilizzata per l'accesso
    private String password;

    public Persona(String email, String password){
        this.email = email;
        this.password = password;
    }

    /*
    *   Due oggetti della classe persona sono considerabili equals se condividono stessa mail.
    *   La definizione di tale metodo permette di mantenere l'univocità della email e di
    *   semplice la ricerca dell'utente durante la fase di login
    *   e
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof Persona otherPersona &&
                this.email.equals(otherPersona.email);
    }

    /*
    *   Metodo che verifica la correttezza delle credenziali inserite.
    *   Dato un oggetto Persona, passato come parametro, verifica che combacino:
    *     - email
    *     - password
    * */
    public boolean controlloCredenziali(Persona p){
        return this.email.equals(p.email) &&
                this.password.equals(p.password);
    }
    //Ridefiniamo il metodo hashCode, coerente con il metodo equals, in modo da poter inserire gli oggetti persona in un hashSet
    @Override
    public int hashCode(){
        return email.hashCode();
    }

    public String toString(){
        return this.email;
    }
}
