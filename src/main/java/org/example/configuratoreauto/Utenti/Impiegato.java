package org.example.configuratoreauto.Utenti;

public class Impiegato extends Persona{
    public Impiegato(String email, String password){
        super(email, password);
        super.userType = TipoUtente.impiegato;
    }
}
