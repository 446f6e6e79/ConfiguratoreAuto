package org.example.configuratoreauto.Preventivi;

public enum StatoPreventivo {
    SCADUTO("Scaduto"),
    RITIRATO("Ritirato"),
    RICHIESTO("Richiesto"),
    FINALIZZATO("Finalizzato"),
    PAGATO("Pagato"),
    DISPONIBILE_AL_RITIRO("Disponibile al ritiro");

    private final String displayName;

    StatoPreventivo(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}