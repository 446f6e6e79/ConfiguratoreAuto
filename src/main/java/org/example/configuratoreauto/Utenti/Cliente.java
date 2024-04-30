package org.example.configuratoreauto.Utenti;

import java.io.Serializable;

public class Cliente extends Persona implements Serializable {
    private String name;
    private String surname;
    public Cliente(String email, String password, String name, String surname){
        super(email, password);
        this.name = name;
        this.surname = surname;
    }

    public String toString(){
        return super.toString() + ", nome: "+name;
    }

    public String getName(){
        return this.name;
    }
}
