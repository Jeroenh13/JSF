/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochconsole;

import calculate.Edge;
import calculate.KochFractal;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import timeutil.TimeStamp;

/**
 *
 * @author Gebruiker
 */
public class MemoryMappedFile implements Observer {

    private final KochFractal kochFractal;
    private final Scanner scanner;
    private int selectedLevel;
    private RandomAccessFile ras;
    private FileChannel fc;
    private MappedByteBuffer out;
    private int edgeCount;
    private int curPos;
    private FileLock lock = null;

    public MemoryMappedFile() {
        scanner = new Scanner(System.in);
        kochFractal = new KochFractal();
        edgeCount = 0;
        kochFractal.addObserver(this);
        curPos = 0;
    }

    public static void main(String[] args) {
        System.out.println("MemoryMappedBuffer!");
        System.out.println("Which level needs to be generated?");
        MemoryMappedFile kConsole = new MemoryMappedFile();
        kConsole.SaveToFile("/MappedFile.bin");
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
        try {
            f.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        kochFractal.setLevel(selectedLevel);
        try {
            ras = new RandomAccessFile(dir, "rw");
            fc = ras.getChannel();
            out = fc.map(FileChannel.MapMode.READ_WRITE, 0, kochFractal.getNrOfEdges() * (7 * 8) + 4 + 4);

            lock = fc.lock(0, 8, false);
            out.position(0);
            out.putInt(edgeCount);
            curPos = curPos + 4;
            out.putInt(selectedLevel);
            curPos = curPos + 4;
            lock.release();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
        ts.setEnd();
        System.out.println(ts.toString());
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        try {
            lock = fc.lock((curPos*8), 56, false);

            out.putDouble(e.X1);
            out.putDouble(e.Y1);
            out.putDouble(e.X2);
            out.putDouble(e.Y2);
            out.putDouble(e.color.getRed());
            out.putDouble(e.color.getGreen());
            out.putDouble(e.color.getBlue());
            curPos = curPos + 7;

            edgeCount++;
            out.putInt(0, edgeCount);
            
            lock.release();
        } catch (IOException ex) {
            Logger.getLogger(MemoryMappedFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
