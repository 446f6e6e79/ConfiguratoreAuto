    package org.example.configuratoreauto.Utenti;

    import org.example.configuratoreauto.AbstractModel;

    /**
    *   Classe UserModel: rappresenta il MODELLO per quanto riguarda gli utenti e l'accesso.
    *   Oltre ai metodi già definiti nella superclasse AbstractModel, la seguente classe definisce i seguenti metodi:
    *       - getInstance() -> ritorna un'istana della classe, implementando il design pattern SINGLETON
    *       - getPathToData() -> resituisce il path al file, contenente i dati serializzati
    *       - registraCliente() -> registra un nuovo cliente, solamente se non già presente
    *       - validation(String email, String Password) -> verifica che esista nei dati salvati delle credenziali combacianti
    *           con quelle inserite
    *       - getCurrentUser() -> restituisce l'utente che ha effettuato il login
    *
    * */
    public class UserModel extends AbstractModel<Persona> {

        private static UserModel instance;

        //Percorso al file contenete le informazioni riguardanti gli utenti
        private static final String USER_PATH = "src/main/resources/data/userData.ser";

        //Variabile currentUser, memorizza l'utente che ha eseguito il login
        private Persona currentUser = null;

        private UserModel() {
            super();
        }
        public static UserModel getInstance() {
            if (instance == null) {
                instance = new UserModel();
            }
            return instance;
        }

        @Override
        protected String getPathToData() {
            return USER_PATH;
        }

        //Controlla che il cliente sia presente nella lista, altrimenti lo aggiunge
        public boolean registraCliente(Cliente newCliente){
            if(data.contains(newCliente)){
                return false;
            }
            return super.addData(newCliente);
        }

        public boolean validation(String email, String password){
            if(email != null && password != null) {

                Persona loginData = new Persona(email, password);
                for (Persona p : data) {
                    if (p.controlloCredenziali(loginData)) {
                        currentUser = p;
                        return true;
                    }
                }
            }
            //Non è stato trovato alcun utente con quelle credenziali
            return false;
        }
        public Persona getCurrentUser(){
            return currentUser;
        }

    }
