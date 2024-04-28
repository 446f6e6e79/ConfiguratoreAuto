    package org.example.configuratoreauto.Utenti;

    import org.example.configuratoreauto.AbstractModel;

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
        public boolean register(Cliente newCliente){
            if(data.contains(newCliente)){
                return false;
            }
            return super.addData(newCliente);
        }

        public boolean login(String email, String password){
            Persona loginData = new Persona(email, password);

            for(Persona p: data){
                if(p.controlloCredenziali(loginData)){
                    currentUser = p;
                    return true;
                }
            }
            //Non è stato trovato alcun utente con quelle credenziali
            return false;
        }
        public Persona getCurrentUser(){
            return currentUser;
        }
    }
