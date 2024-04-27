package org.example.configuratoreauto.Utenti;

import java.io.*;
import java.util.HashSet;

public class UserModel{
    //Definiamo un unica istanza di Model, condivisa da tutti gli oggetti della classe UserModel
    private static UserModel instance;
    //Percorso al file contenete le informazioni riguardanti gli utenti
    private static final String USER_PATH = "src/main/resources/data/userData.ser";
    //Hash set che conterrà l'elenco di tutte le utenze
    private HashSet<Persona> usersSet;
    //Variabile currentUser, memorizza l'utente che ha eseguito il login
    private Persona currentUser = null;

    /*
        getInstance: Ritorna l'istanza del modello:
            - alla prima chiamata il modello non è istanziato, viene generato dal costruttore
            - alle chiamate successive, viene restituita la prima istanza generata
    */
    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    //Costruttore della classe UserModel, genera l'oggetto per la lettura del file.json e carica gli utenti
    private UserModel(){
        usersSet = new HashSet<>();
        loadUsers();
    }

    /*
    *   La funzione loadUsers carica gli utenti registrati all'interno dell'HashSet.
    *       - Gli utenti sono memorizzati in formato json all'interno del file userData
    *       -
    * */
    private void loadUsers(){
        try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(USER_PATH))){
            //Oggetto temporaneo, rappresenta il singolo oggetto, letto dal file
            Object tempObj;
            while ((tempObj = objectInput.readObject()) != null) {
                usersSet.add((Persona) tempObj);
            }
        }
        //Terminata la lettura dal file. Stampa la lista degli utenti a video, per debug
        catch (EOFException e){
            printUsers();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     *  La funzione uploadUserUpdates riscrive il file di testo, aggiornando i dati al suo interno.
     * */
    public void uploadUserUpdates() {
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(USER_PATH))) {
            for(Persona p: usersSet){
                objectOutput.writeObject(p);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *   La funzione register aggiunge un cliente alla lista degli utenti.
    *   - Ritorna TRUE, se il cliente non era presente nella lista (la sua email era già presente tra quelle registrate)
    *   - Ritorna FALSE, se era già presente un cliente con uguale mail
    *
    * */
    public boolean register(Cliente newCliente){
        return usersSet.add(newCliente);
    }

    public boolean login(String email, String password){
        Persona person = new Persona(email, password);

        //Se non è presente alcun utente con quella mail ritorno falso
        if(!usersSet.contains(person)) {
            return false;
        }
        //Scorro il set, in cerca di un utente con stesse credenziali di login (email, password)
        for(Persona tmp: usersSet){
            if(tmp.controlloCredenziali(person)){
                currentUser = tmp;
                return true;
            }
        }
        //Se non ho trovato alcun utente, ritorno false
        return false;
    }
    public Persona getCurrentUser(){
        return currentUser;
    }
    private void printUsers(){
        for(Persona p: usersSet){
            System.out.println(p);
        }
    }

    public void addGenericUser(Persona p){
        usersSet.add(p);
    }
}
