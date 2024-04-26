package org.example.configuratoreauto.Utenti;

public class Segretario extends Persona {

    public Segretario(String email, String password){
        super(email, password);
        super.userType = TipoUtente.segretario;
    }
}
