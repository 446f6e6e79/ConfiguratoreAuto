package org.example.configuratoreauto.Utenti;

import java.io.Serial;
import java.io.Serializable;

public class Impiegato extends Persona implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Impiegato(String email, String password){
        super(email, password);
    }
}
