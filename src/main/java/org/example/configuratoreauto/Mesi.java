package org.example.configuratoreauto;

public enum Mesi {
    GENNAIO,
    FEBBRAIO,
    MARZO,
    APRILE,
    MAGGIO,
    GIUGNO,
    LUGLIO,
    AGOSTO,
    SETTEMBRE,
    OTTOBRE,
    NOVEMBRE,
    DICEMBRE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
