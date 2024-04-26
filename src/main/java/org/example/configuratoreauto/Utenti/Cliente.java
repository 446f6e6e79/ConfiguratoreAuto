package org.example.configuratoreauto.Utenti;

public class Cliente extends Persona{
    private String name;
    private String surname;
    public Cliente(String email, String password){
        super(email, password);
        super.userType = TipoUtente.cliente;
    }
}
