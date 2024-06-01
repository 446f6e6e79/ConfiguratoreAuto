package org.example.configuratoreauto.Utenti;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Persona implements Serializable{
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    /** Email della persona, utilizzata come credenziale per il login */
    private String email;
    /** Password, utilizzata per l'accesso */
    private String password;

    /**
     * Costruttore per la classe PERSONA
     * @throws IllegalArgumentException se l'indirizzo email inserito non è valido
     */
    public Persona(String email, String password){
        if(!isValidEmail(email)){
            throw new IllegalArgumentException("Email non valida");
        }
        this.email = email.toLowerCase();
        this.password = password;
    }

    /**
    *   Controllo sulla validità dell'email inserita
    * */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
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

    /**
    *   Metodo che verifica la correttezza delle credenziali inserite, implementando
     *   la funzione di LOGIN
    *   Dato un oggetto Persona, passato come parametro, verifica che combacino:
    *     - email
    *     - password
    * */
    public boolean controlloCredenziali(Persona p){
        return equals(p);
    }
}
