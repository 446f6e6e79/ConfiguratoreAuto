package org.example.configuratoreauto.Macchine;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.Comparator;

/*
*   PATH: percorso assoluto all'immagine, inserita come input
*   COLORE: colore optional, a cui l'immagine fa riferimento
* */
public class Immagine implements Serializable{
    String path;
    String colore;
    Auto auto;

    public Immagine(String colore, Auto auto, String absolutePath){
        this.colore=colore;
        this.path=absolutePath;
        this.auto=auto;
    }

    public String getColor(){
        return this.colore;
    }

    public Image getImage() {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
            return null;
        }
    }

    public static void cleanDirectory(AutoNuova autoNuova) throws IOException {
        Path path = Paths.get("src", "main", "resources", "img", "carImages", String.valueOf(autoNuova.getId()));
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete " + p, e);
                        }
                    });
        }
    }

    public void addToLocalImages() {
        if(auto instanceof AutoNuova autoNuova){
            for(Immagine img:auto.getImmagini()){
                addAutoNuova(autoNuova);
            }

        }
        else {
            for(Immagine img:auto.getImmagini()){
                addAutoUsata((AutoUsata) auto);
            }
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
    private void addAutoNuova(AutoNuova auto){
        Path root = Paths.get("src", "main", "resources", "img", "carImages", String.valueOf(auto.getId()));
        Path target = root.resolve(colore);
        Path source = Paths.get(this.path);

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

            //Aggiorno il path dell'immagine
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
    private void addAutoUsata(AutoUsata auto){
        Path target = Paths.get("src", "main", "resources", "img", "usedCarImages", String.valueOf(auto.getTarga()));
        Path source = Paths.get(this.path);
        
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

            //Aggiorno il path dell'immagine
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
