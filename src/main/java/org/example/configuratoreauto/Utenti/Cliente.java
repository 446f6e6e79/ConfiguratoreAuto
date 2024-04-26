package org.example.configuratoreauto.Utenti;

public class Cliente extends Persona{

    public Cliente(String email, String password){
        super(email, password);
        super.userType = TipoUtente.cliente;
    }
}
