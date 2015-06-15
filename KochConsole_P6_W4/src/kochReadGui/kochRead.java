/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochReadGui;

import calculate.Edge;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author jsf3
 */
public class kochRead {

    public JSF31KochFractalFX application;
    private TimeStamp ts;
    private TimeStamp ts2;
    ArrayList<Edge> edges;
    WatchService watcher;

    public kochRead(JSF31KochFractalFX application) {
        this.application = application;
        Path dir = Paths.get("/hddJeroen/kochFiles/");
        try {
            watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = dir.register(watcher, ENTRY_MODIFY);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public void drawEdges() {
        ts = new TimeStamp();
        ts.setBegin();
        application.clearKochPanel();
        for (Edge e : edges) {
            application.drawEdge(e);
        }
        ts.setEnd();
        application.setTextDraw(ts.toString());
    }

    public void changeLevel(int i) {

    }

    public void readFile(boolean b) {
        ts2 = new TimeStamp();
        ts2.setBegin();
        edges = new ArrayList<Edge>();
        //no buffer
        if (!b) {
            try {
                Path f = Paths.get("/hddJeroen/kochFiles/TekstNoBuffer");
                List<String> fileList = Files.readAllLines(f);
                for (String s : fileList) {
                    if ("Se".equals(s.substring(0, 2))) {
                        application.setTextLevel("Level: ".concat(s.substring(16)));
                    } else {
                        String[] edge = s.split("([,])");

                        Double d1 = Double.parseDouble(edge[4]);
                        Double d2 = Double.parseDouble(edge[5]);
                        Double d3 = Double.parseDouble(edge[6]);
                        Color c = Color.hsb(Double.parseDouble(edge[4]), Double.parseDouble(edge[5]), Double.parseDouble(edge[6]));
                        Edge e = new Edge(Double.parseDouble(edge[0]), Double.parseDouble(edge[1]), Double.parseDouble(edge[2]), Double.parseDouble(edge[3]), c);
                        edges.add(e);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        } //with buffer
        else {
            FileReader reader;
            BufferedReader buffer;
            try {
                reader = new FileReader("/hddJeroen/kochFiles/TekstWBuffer");
                buffer = new BufferedReader(reader);
                String line;
                while ((line = buffer.readLine()) != null) {
                    if ("Se".equals(line.substring(0, 2))) {
                        application.setTextLevel("Level: ".concat(line.substring(16)));
                    } else {
                        String[] edge = line.split("([,])");

                        Double d1 = Double.parseDouble(edge[4]);
                        Double d2 = Double.parseDouble(edge[5]);
                        Double d3 = Double.parseDouble(edge[6]);
                        Color c = Color.hsb(Double.parseDouble(edge[4]), Double.parseDouble(edge[5]), Double.parseDouble(edge[6]));
                        Edge e = new Edge(Double.parseDouble(edge[0]), Double.parseDouble(edge[1]), Double.parseDouble(edge[2]), Double.parseDouble(edge[3]), c);
                        edges.add(e);
                    }
                }

                buffer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ts2.setEnd();
        application.setTextCalc(ts2.toString());
        application.requestDrawEdges();
    }

    public void readBinary(boolean b) {
        ts2 = new TimeStamp();
        ts2.setBegin();
        edges = new ArrayList<Edge>();
        //no buffer
        if (!b) {
            try {
                File f = new File("/hddJeroen/kochFiles/BinaryNoBuffer");
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                int line = (int) in.readObject();
                application.setTextLevel("Level: ".concat(Integer.toString(line)));

                while (true) {
                    Object o;
                    try {
                        o = in.readObject();
                    } catch (EOFException e) {
                        break;
                    }
                    if (o instanceof Edge) {
                        Edge temp = (Edge) o;
                        edges.add(new Edge(temp.X1, temp.Y1, temp.X2, temp.Y2, Color.WHITE));
                    }
                }
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        } //with buffer
        else {
            try {
                File f = new File("/hddJeroen/kochFiles/BinaryWBuffer");
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
                int line = (int) in.readObject();
                application.setTextLevel("Level: ".concat(Integer.toString(line)));

                while (true) {
                    Object o;
                    try {
                        o = in.readObject();
                    } catch (EOFException e) {
                        break;
                    }
                    if (o instanceof Edge) {
                        Edge temp = (Edge) o;
                        edges.add(new Edge(temp.X1, temp.Y1, temp.X2, temp.Y2, Color.WHITE));
                    }
                }

                in.close();
            } catch (IOException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ts2.setEnd();
        application.setTextCalc(ts2.toString());
        application.requestDrawEdges();
    }

    public void readMMB() {
        ts2 = new TimeStamp();
        ts2.setBegin();
        edges = new ArrayList<Edge>();

        int count = 0;

        RandomAccessFile ras;
        FileChannel fc;
        MappedByteBuffer out;

        try {
            ras = new RandomAccessFile("/hddJeroen/kochFiles/MemoryMappedFile", "rw");
            fc = ras.getChannel();
            out = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        int line = out.getInt();
        application.setTextLevel("Level: ".concat(Integer.toString(line)));

        int edgeCount = out.getInt();
        int i = 0;
        while (edgeCount >= i && out.remaining() >= 36) {
            Edge e = new Edge(
                    out.getDouble(),
                    out.getDouble(),
                    out.getDouble(),
                    out.getDouble(),
                    new Color(
                            out.getDouble(),
                            out.getDouble(),
                            out.getDouble(),
                            1)
            );
            edges.add(e);
            i++;
        }

        ts2.setEnd();
        application.setTextCalc(ts2.toString());
        application.requestDrawEdges();
    }
}
