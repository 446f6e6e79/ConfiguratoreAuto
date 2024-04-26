package org.example.configuratoreauto.Utenti;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class UserModel{

    //
    private static Gson gson;
    //Definiamo un unica istanza di Model, condivisa da tutti gli oggetti della classe UserModel
    private static UserModel instance;
    private static final String USER_PATH = "src/main/resources/data/userData.json";
    //Hash set che conterrà l'elenco di tutte le utenze
    private HashSet<Persona> usersSet;
    //Variabile currentUser, memorizza l'utente che ha eseguito il login
    private Persona currentUser = null;

    /*
        Ritorna l'istanza del modello:
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
        gson = new GsonBuilder().setPrettyPrinting().create();
        loadUsers();
    }

    /*
    *   La funzione loadUsers carica gli utenti registrati all'interno dell'HashSet.
    *       - Gli utenti sono memorizzati in formato json all'interno del file userData
    *       -
    * */
    private void loadUsers(){
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(USER_PATH)));
            Type personaListType = new TypeToken<HashSet<Persona>>() {}.getType();
            usersSet = gson.fromJson(jsonString, personaListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printUsers();
    }

    /*
     *  La funzione uploadUserUpdates riscrive il file di testo, aggiornando i dati al suo interno.
     * */
    public void uploadUserUpdates() {
        try (FileWriter writer = new FileWriter(USER_PATH)) {
            String usrJson = gson.toJson(usersSet);
            writer.write(usrJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *   La funzione register aggiunge un cliente alla lista degli utenti.
    *   - Ritorna TRUE, se il cliente non era presente nella lista
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
}
