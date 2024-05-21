package org.example.configuratoreauto.Macchine;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
*   PATH: percorso assoluto all'immagine, inserita come input
*   COLORE: colore optional, a cui l'immagine fa riferimento
* */
public class Immagine implements Serializable{
    String path;
    String colore;

    public Immagine(String colore, Auto auto, String absolutePath){
        this.colore=colore;
        addToLocalImages(auto, absolutePath);
    }

    public String getColor(){
        return this.colore;
    }

    public Image getImage() {
        System.out.println(path);
        try {
            FileInputStream inputStream = new FileInputStream(path);
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina il file dalla relativa directory
     */
    public void delete(){
        try {
            Files.delete(Path.of(this.path));
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    /**
    *   Metodo STATICO. Passato come parametri una LISTA DI IMMAGINI ed un COLORE
    *       restituisce la stessa lista, filtrata per colore
    * */
    public static ArrayList<Immagine> getImagesByColor(ArrayList<Immagine> imageList, String color) {
        return imageList.stream()
                .filter(t -> t.colore.equals(color))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void addToLocalImages(Auto auto, String absolutePath) {
        if(auto instanceof AutoNuova autoNuova){
            addAutoNuova(autoNuova, absolutePath);
        }
        else {
            addAutoUsata((AutoUsata) auto, absolutePath);
        }
    }

    /**
        Aggiungo l'immagine alla DIRECTORY interna al progetto:
            - Le cartelle sono gestite nel seguente modo:
                -carImages
                   -idAuto_1
                      -colore_1
                      ...
                      -colore_n
                   -idAuto_2
                   ...
    */
    private void addAutoNuova(AutoNuova auto, String absolutePath){
        Path root = Paths.get("src", "main", "resources", "img", "carImages", String.valueOf(auto.getId()));
        Path target = root.resolve(colore);
        Path source = Paths.get(absolutePath);

        //Creo la directory root se non già esistente
        if (Files.notExists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                System.err.println("Error creating directories: " + e.getMessage());
                return;
            }
        }
        //Creo la directoryTarget se non già esistente
        if (Files.notExists(target)) {
            try {
                Files.createDirectories(target);
            } catch (IOException e) {
                System.err.println("Error creating target directory: " + e.getMessage());
                return;
            }
        }
        /*
        *   Copio il file SORGENTE nel percorso DESTINAZIONE, all'interno del progetto
        * */
        try {
            Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            //Salvo il path dell'immagine
            this.path = String.valueOf(target.resolve(source.getFileName()));
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists in target directory");
        } catch (NoSuchFileException e) {
            System.err.println("Source file not found");
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    /**
        Aggiungo l'immagine dell'auto usata alla DIRECTORY interna al progetto:
            - Le cartelle sono gestite nel seguente modo:
                -usedCarImages
                   -TARGA_1
                   -TARGA_2
                   ...
                   -TARGA_N
    */
    private void addAutoUsata(AutoUsata auto, String absolutePath){
        Path target = Paths.get("src", "main", "resources", "img", "carImages", String.valueOf(auto.getTarga()));
        Path source = Paths.get(absolutePath);

        if (Files.notExists(target)) {
            try {
                Files.createDirectories(target);
            } catch (IOException e) {
                System.err.println("Error creating directories: " + e.getMessage());
                return;
            }
        }
        try {
            Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            //Salvo il path dell'immagine
            this.path = String.valueOf(target.resolve(source.getFileName()));
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists in target directory");
        } catch (NoSuchFileException e) {
            System.err.println("Source file not found");
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }
    @Override
    public boolean equals(Object other){
        return other instanceof Immagine otherImage &&
                this.path.equals(otherImage.path) &&
                this.colore.equals(otherImage.colore);
    }
}
