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
import java.nio.channels.FileLock;
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
    private FileLock lock = null;

    public kochRead(JSF31KochFractalFX application) {
        this.application = application;
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
                Path p = Paths.get("/");
                kochDirWatchable watcher = new kochDirWatchable(p, false);

                Thread t = new Thread(watcher);
                t.start();
                while (true) {
                    if (watcher.getDone()) {
                        File f = new File("/BinaryWBuffer.bin");
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
                                //System.out.println(edges.size());
                            }
                        }

                        in.close();
                    }
                    break;
                }
            } catch (IOException | ClassNotFoundException ex) {
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

        application.clearKochPanel();
        try {
            ras = new RandomAccessFile("/MappedFile.bin", "r");
            fc = ras.getChannel();

            new Thread(new Runnable() {
                boolean finished = false;
                int c = 0;

                @Override
                public void run() {
                    try {
                        MappedByteBuffer out;
                        out = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                        lock = fc.lock(0, 8, true);
                        int edgeCount = out.getInt();
                        application.setTextNrEdges(Integer.toString(edgeCount));

                        int line = out.getInt();
                        application.setTextLevel("Level: ".concat(Integer.toString(line)));

                        lock.release();
                        while (!finished) {
                            //while (c < edgeCount) {
                            int lockEdgeLength = (4 * 8) + (3 * 8);
                            int start = 8;
                            start += lockEdgeLength;
                            lock = fc.lock(start, lockEdgeLength, true);
                            out.position(c * (7 * 8) + 8);
                            double X1 = out.getDouble();
                            double Y1 = out.getDouble();
                            double X2 = out.getDouble();
                            double Y2 = out.getDouble();
                            double red = out.getDouble();
                            double green = out.getDouble();
                            double blue = out.getDouble();

                            Edge e = new Edge(X1, Y1, X2, Y2,
                                    new Color(red, green, blue, 1)
                            );
                            edges.add(e);
                            application.drawEdge(e);
                            // Thread.sleep(1);
                            c++;
                            finished = (edgeCount == c);
                            lock.release();

                            lock = fc.lock(0, 4, true);
                            edgeCount = out.getInt(0);
                            lock.release();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(kochRead.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }).start();

            ts2.setEnd();
            application.setTextCalc(ts2.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
