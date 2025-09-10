package TP2.ej5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Escribir un programa que reciba como parámetro un directorio y devuelva la cantidad de líneas totales que contienen los archivos de dicho directorio.
 * Para hacer esta tarea de manera paralela se pide que dicho programa genere un thread por cada archivo y que la tarea a ejecutar por dicho thread sea
 * contar las lineas de dicho archivo o devolver 0 si el archivo era en realidad un directorio.
 * Una vez que los threads han terminado sumarizar y imprimir en pantalla la cantidad total.
 *
 * @source https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html
 * @source https://www.w3schools.com/java/java_files_read.asp
 * @source https://www.baeldung.com/java-list-directory-files
 * @source https://www.techrepublic.com/article/java-directory-navigation/
 */

public class LineCounter implements Callable<Integer> {
    private File file;

    public LineCounter(File file){
        this.file = file;
    }

    private Integer countLinesInFile(File file){
        if(file.isDirectory() || !file.canRead()){
            return 0;
        }

        Scanner scanner;
        Integer lines = 0;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e){
            System.out.println("Error contando lineas! FileNotFoundException...");
            return 0;
        }

        while(scanner.hasNextLine()){
            lines++;
            scanner.nextLine();
        }

        return lines;
    }

    @Override
    public Integer call() throws Exception {
        return countLinesInFile(this.file);
    }

    public static void main(String[] args) {
        String directory = "/Users/tomaspinausig/";
        File dir = new File(directory);
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future<Integer>> futures = new ArrayList<>();
        Integer totalLines = 0;

        for(File file : dir.listFiles()){
           futures.add(pool.submit(new LineCounter(file)));
        }

        for(Future<Integer> future : futures){
            try{
                totalLines += future.get();
            } catch(Exception e){
                System.out.println(e);
            }
        }

        System.out.println("Lineas totales: " + totalLines);

        pool.shutdown();
        try{
            if(!pool.awaitTermination(800, TimeUnit.MILLISECONDS)){
                pool.shutdownNow();
            }
        } catch (InterruptedException e){
            return;
        }
    }
}


