/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochconsole;

import calculate.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import timeutil.TimeStamp;

/**
 *
 * @author jsf3
 */
public class BinaryWBuffer implements Observer {

    private final KochFractal kochFractal;
    private final Scanner scanner;
    private File file;
    private FileOutputStream fos;
    private ObjectOutputStream out;
    private int selectedLevel;

    public BinaryWBuffer() {
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
        BinaryWBuffer kConsole = new BinaryWBuffer();
        kConsole.SaveToFile("/hddJeroen/kochFiles/BinaryWBuffer");
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
            fos = new FileOutputStream(f);
            out = new ObjectOutputStream(new BufferedOutputStream(fos));
            out.writeObject(selectedLevel);
            out.writeObject("\n");
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        kochFractal.setLevel(selectedLevel);
        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
        try {
            out.close();
            ts.setEnd();
            System.out.println(ts.toString());
        } catch (IOException ex) {

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        try {
            Edge e2 = new Edge(e.X1,e.Y1,e.X2,e.Y2);
            out.writeObject(e2);
            out.writeObject("\n");
        } catch (IOException ex) {

        }
    }
}
