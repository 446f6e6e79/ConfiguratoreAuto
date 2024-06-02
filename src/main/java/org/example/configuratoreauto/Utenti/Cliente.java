package org.example.configuratoreauto.Utenti;

import java.io.Serial;
import java.io.Serializable;

public class Cliente extends Persona implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String surname;
    private static final String NAME_SURNAME_REGEX = "[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\s\\-]+";

    /**
     * Crea un'istanza della classe Cliente. Viene verificato che
     * nome e cognome siano validi.
     * @throws IllegalArgumentException Se il nome o il cognome contengono caratteri non validi
     */

    public Cliente(String email, String password, String name, String surname) {
        super(email, password);
        if (!name.matches(NAME_SURNAME_REGEX)) {
            throw new IllegalArgumentException("Nome non valido");
        }
        if (!surname.matches(NAME_SURNAME_REGEX)) {
            throw new IllegalArgumentException("Cognome non valido");
        }
        this.name = name;
        this.surname = surname;
    }

    public String toString(){
        return super.toString() + ", nome: "+name;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname() {
        return surname;
    }
}
