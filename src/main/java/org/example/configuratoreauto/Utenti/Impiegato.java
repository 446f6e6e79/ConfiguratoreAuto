package org.example.configuratoreauto.Utenti;

import java.io.Serializable;

public class Impiegato extends Persona implements Serializable {
    public Impiegato(String email, String password){
        super(email, password);
    }
}
