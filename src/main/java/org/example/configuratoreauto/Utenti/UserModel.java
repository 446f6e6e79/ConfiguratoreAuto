    package org.example.configuratoreauto.Utenti;

    import org.example.configuratoreauto.AbstractModel;

    /**
    *   Classe <u>UserModel</u>: rappresenta il MODELLO per quanto riguarda gli utenti e l'accesso.
    *<p>   Oltre ai metodi già definiti nella superclasse AbstractModel, la seguente classe definisce i seguenti metodi:
    *<p>      - getInstance() -> ritorna un'istana della classe, implementando il design pattern SINGLETON
    *<p>       - getPathToData() -> resituisce il path al file, contenente i dati serializzati
    *<p>       - registraCliente() -> registra un nuovo cliente, solamente se non già presente
    *<p>       - validation(String email, String Password) -> verifica che esista nei dati salvati delle credenziali combacianti
    *           con quelle inserite
    *<p>       - getCurrentUser() -> restituisce l'utente che ha effettuato il login
    * */
    public class UserModel extends AbstractModel<Persona> {

        private static UserModel instance;

        /** Percorso al file contenete le informazioni riguardanti gli utenti */
        private static final String USER_PATH = "src/main/resources/data/userData.ser";

        /** Variabile currentUser, memorizza l'utente che ha eseguito il login */
        private Persona currentUser = null;

        /**
         * Costruttore privato. In questo modo possiamo implementare il pattern <u>SINGLETON</u>.
         <p>Verrà crerata un <u>istanza Unica</u> della classe CatalogoModel, utilizzata per
         *  l'intera durata dell'esecuzione
         */
        private UserModel() {
            super();
        }

        /**
         * Restituisce un istanza della Classe UserModel
         */
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

        /** Controlla che il cliente sia presente nella lista, altrimenti lo aggiunge */
        public boolean registraCliente(Cliente newCliente){
            if(data.contains(newCliente)){
                return false;
            }
            return super.addData(newCliente);
        }

        /** Controllo sul LOGIN da parte dell'utente.
         *  Viene verificato che email e password inserite siano
         *  presenti all'interno del modello.
         * <p>
         *  Se presenti
         */
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

        /** Ritorna l'utente attualmente connesso */
        public Persona getCurrentUser(){
            return currentUser;
        }

        public boolean isCliente(){
            return currentUser instanceof Cliente;
        }

        public boolean isImpiegato(){
            return currentUser instanceof Impiegato;
        }

        public boolean isSegretario(){
            return currentUser instanceof Segretario;
        }

        public void clearCurrentUser(){ currentUser=null; }
    }
