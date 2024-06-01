package org.example.configuratoreauto.Macchine;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.Comparator;

public class Immagine implements Serializable{
    /** percorso assoluto all'immagine, inserita come input */
    private String path;
    /** colore optional, a cui l'immagine fa riferimento */
    private String colore;

    /** Percorso alla cartella di immagini Temporanee */
    private static Path tempImages = Paths.get("src", "main", "resources", "img", "carImages", "tempImages");

    public Immagine(String colore, String absolutePath){
        this.colore=colore;
        this.path=absolutePath;
    }

    public String getColor(){
        return this.colore;
    }

    /**
     * Restituisce l'oggetto  di tipo Image, assegnabile ad ImageView
     * @return Image relativa all'immagine
     */
    public Image getImage() {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
            return null;
        }
    }

    /**
     *  Rinomina la directory TEMPORANEA, nella directory TARGET, in cui dovranno essere inserite le immagini.
     *  Rimpiazza eventualmente, le cartelle TARGET già presenti, tenendo così aggiornate le immagini.
     *  Rimuove inoltre infine la cartella tempImages
     * @param autoNuova - Auto per cui stiamo cercando di aggiungere le immagini
     * @throws IOException
     */
    public static void cleanAndRenameDirectory(AutoNuova autoNuova) throws IOException {
        Path rootBase = Paths.get("src", "main", "resources", "img", "carImages");
        Path targetDir = rootBase.resolve(String.valueOf(autoNuova.getId()));
        Path tempDir = rootBase.resolve("tempImages");

        /*
            Pulisco la cartella target, se era già esistente. Non ho problemi di eliminare
            modifiche effettuate alle immagini, in quanto quelle sono salvate nella direcory
            tempAuto
         */
        cleanDirectory(targetDir);

        //Rinomino la carrella tempImages, nella cartella target
        if (Files.exists(tempDir)) {
            Files.move(tempDir, targetDir, StandardCopyOption.REPLACE_EXISTING);
        }
        cleanDirectory(tempDir);
    }



    /* Il fatto di mantenere una cartella TEMPORANEA mi permette di
    *  mantenere una directory pulita. Andando, con maggiore semplicità a gestire
    *  l'eliminazione di immagini, semplicemente sovrascrivedo la cartella target
    */
    public void addAutoNuova(AutoNuova auto){
        Path baseRoot = Paths.get("src", "main", "resources", "img", "carImages");
        Path target = tempImages.resolve(colore);
        Path source = Paths.get(this.path);

        //Creo la directory tempImages, se non già presente
        if(Files.notExists(tempImages)) {
            try {
                Files.createDirectories(tempImages);
            } catch (IOException e) {
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

            /*
                Aggiorno il path dell'immagine.
             */
            this.path = String.valueOf(baseRoot.resolve(auto.getId() +"/"+colore+"/"+source.getFileName()));
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
    public void addAutoUsata(AutoUsata auto) {
        Path baseRoot = Paths.get("src", "main", "resources", "img", "usedCarImages");
        Path target = baseRoot.resolve(auto.getTarga());
        Path source = Paths.get(this.path);
        System.out.println("Arrivato 1");
        // Create the target directory if it does not exist
        if (Files.notExists(target)) {
            try {
                Files.createDirectories(target);
            } catch (IOException e) {
                System.err.println("Error creating target directory: " + e.getMessage());
                return;
            }
        }
        System.out.println("Arrivato 2");
        // Copy the source file to the target directory
        try {
            Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arrivato 3");
            // Update the path of the image to the new location
            this.path = String.valueOf(target.resolve(source.getFileName()));
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists in target directory: " + e.getMessage());
        } catch (NoSuchFileException e) {
            System.err.println("Source file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    /**
     *  Rimuove una directory passata come parametro, andando ad eliminare anche
     *  le sotto-cartelle
     * @param dir -Path alla directory che si intende eliminare
     */
    private static void cleanDirectory(Path dir){
        if (Files.exists(dir)) {
            try {
                Files.walk(dir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                System.out.println("Errorore nell'eliminazione del file " + p + ": " + e.getMessage());
                            }
                        });
            } catch (IOException e) {
                System.err.println("Error deleting directory " + dir + ": " + e.getMessage());
            }
        }
    }
}
