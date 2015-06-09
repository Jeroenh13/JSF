/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochconsole;

import calculate.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import timeutil.TimeStamp;

/**
 *
 * @author jsf3
 */
public class TekstWBuffer implements Observer {

    private final KochFractal kochFractal;
    private final Scanner scanner;
    private File file;
    private FileWriter fw;
    private BufferedWriter bw;
    private int selectedLevel;

    public TekstWBuffer() {
        scanner = new Scanner(System.in);
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("With Buffer");
        System.out.println("Which level needs to be generated?");
        TekstWBuffer kConsole = new TekstWBuffer();
        kConsole.SaveToFile("/hddJeroen/kochFiles/TekstWBuffer");
        System.out.println("Done!");
    }

    

    public void SaveToFile(String dir) {
        TimeStamp ts = new TimeStamp();
        ts.setBegin();
        boolean done = false;
        while (!done) {
            try {
                selectedLevel = Integer.parseInt(scanner.nextLine());
                done = true;
            } catch (NumberFormatException e) {
                System.out.println("This needs to be an integer");
            }
        }
        System.out.println("Selected level: " + Integer.toString(selectedLevel));

        File f = new File(dir);
        if (f.exists()) {
            f.delete();
        }
        try{
            f.createNewFile();
        }
        catch(IOException ex){
            
        }
        try {
            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
            bw.write("Selected Level: " + selectedLevel);
            bw.write("\n");
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        kochFractal.setLevel(selectedLevel);
        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
        try {
            bw.close();
            fw.close();
            ts.setEnd();
            System.out.println(ts.toString());
        } catch (IOException ex) {

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        try {
            bw.write(e.toString());
            bw.write("\n");
        } catch (IOException ex) {

        }
    }
}
