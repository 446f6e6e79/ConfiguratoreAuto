package org.example.configuratoreauto.Preventivi;


import org.example.configuratoreauto.AbstractModel;
import org.example.configuratoreauto.Macchine.Alimentazione;
import org.example.configuratoreauto.Macchine.AutoNuova;
import org.example.configuratoreauto.Macchine.Marca;
import org.example.configuratoreauto.Utenti.Cliente;
import org.example.configuratoreauto.Utenti.Impiegato;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RegistroModel extends AbstractModel<Preventivo> {

    private static RegistroModel instance;

    //Percorso al file contenete le informazioni riguardanti gli utenti
    private static final String REGISTRO_PATH = "src/main/resources/data/registro.ser";

    private RegistroModel() {
        super();
    }
    public static RegistroModel getInstance() {
        if (instance == null) {
            instance = new RegistroModel();
        }
        return instance;
    }

    @Override
    protected String getPathToData() {
        return REGISTRO_PATH;
    }

    public ArrayList<Preventivo> getAllPreventivo(){
        return super.data;
    }

    public ArrayList<Preventivo> getPreventivoByBrand(Marca brand){
        return super.data.stream()
                .filter(t -> t.getAcquisto().getMarca() == brand)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> getPreventivoByCliente(Cliente cliente){
        return super.data.stream()
                .filter(t -> t.getCliente().equals(cliente))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> getPreventivoByImpiegato(Impiegato impiegato){
        return super.data.stream()
                .filter(t -> t.getImpiegato().equals(impiegato))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Preventivo> getPreventivoBySede(Sede sede){
        return super.data.stream()
                .filter(t -> t.getSede().equals(sede))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
