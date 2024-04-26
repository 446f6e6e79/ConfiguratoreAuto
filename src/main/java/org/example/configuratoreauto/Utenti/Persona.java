package org.example.configuratoreauto.Utenti;

public abstract class Persona {
    //Email della persona, utilizzata come credenziale per il login
    private String email;

    //Password, utilizzata per l'accesso
    private String password;

    //Specifica il tipo di utente a cui si fa riferimento (cliente, impiegato o persona)
    protected TipoUtente userType;

    public Persona(String email, String password){
        this.email = email;
        this.password = password;
    }

    //Metodo getter per il parametro email
    public String getEmail(){
        return this.email;
    }

    /*
    *   Due oggetti della classe persona sono considerabili equals se condividono:
    *       - stessa email
    *       - stessa password
    *  La definizione di tale metodo renderà più semplice la ricerca dell'utente durante la fase di login
    * */
    @Override
    public boolean equals(Object o){
        return o instanceof Persona otherPersona &&
                this.email.equals(otherPersona.email) &&
                this.password.equals(otherPersona.password);
    }

    //Ridefiniamo il metodo hashCode, coerente con il metodo equals, in modo da poter inserire gli oggetti persona in un hashSet
    @Override
    public int hashCode(){
        return email.hashCode() ^ password.hashCode();
    }

    public TipoUtente getUserType(){
        return userType;
    }
}
