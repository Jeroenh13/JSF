/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochconsole;

import calculate.*;
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
public class TekstNoBuffer implements Observer {

    private final KochFractal kochFractal;
    private final Scanner scanner;
    private File file;
    private FileWriter fw;
    private int selectedLevel;

    public TekstNoBuffer() {
        scanner = new Scanner(System.in);
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("No Buffer!");
        System.out.println("Which level needs to be generated?");
        TekstNoBuffer kConsole = new TekstNoBuffer();
        kConsole.SaveToFile("/hddJeroen/kochFiles/TekstNoBuffer");
        System.out.println("Done!");
    }

    

    public void SaveToFile(String dir) {
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
        
        TimeStamp ts = new TimeStamp();
        ts.setBegin();
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
            fw.write("Selected Level: " + selectedLevel);
            fw.write("\n");
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        kochFractal.setLevel(selectedLevel);
        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
        try {
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
            fw.write(e.toString());
            fw.write("\n");
        } catch (IOException ex) {

        }
    }
}
