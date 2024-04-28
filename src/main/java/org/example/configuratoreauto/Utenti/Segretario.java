package org.example.configuratoreauto.Utenti;

import java.io.Serializable;

public class Segretario extends Persona implements Serializable {
    public Segretario(String email, String password){
        super(email, password);
    }
}
