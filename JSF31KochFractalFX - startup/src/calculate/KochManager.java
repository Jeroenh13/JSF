/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;


/**
 *
 * @author jsf3
 */
public class KochManager
{
    public JSF31KochFractalFX application;
    public ArrayList<Edge> edges;
    private TimeStamp ts;
    private TimeStamp ts2;
    private int counter = 0;
    private Thread tLeft;
    private Thread tRight;
    private Thread tBottom;
    
    private KochFractal kochLeft;
    private KochFractal kochRight;
    private KochFractal kochBottom;
    
    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;
        kochLeft = new KochFractal();
        kochRight = new KochFractal();
        kochBottom = new KochFractal();
        edges = new ArrayList<>();
    }    

    public synchronized void changeLevel(int nxt) {
        changeLevels(nxt);
        edges.clear();
        ts2 = new TimeStamp();
        ts2.setBegin();
        
        //create runnables and threads
        kochRunnable krl = new kochRunnable("Left",this, kochLeft);
        kochRunnable krb = new kochRunnable("Bottom",this, kochRight);
        kochRunnable krr = new kochRunnable("Right",this, kochBottom);
        tLeft = new Thread(krl);
        tRight = new Thread(krr);
        tBottom = new Thread(krb);
        
        //start the threads
        startThreads();       
        
        //ts2.setEnd();
        application.setTextNrEdges(String.valueOf(getEdges()));
        
    }
    
    void changeLevels(int nxt)
    {
        kochLeft.setLevel(nxt);
        kochRight.setLevel(nxt);
        kochBottom.setLevel(nxt);
    }
    
    
    int getEdges()
    {
        return kochLeft.getNrOfEdges() + kochRight.getNrOfEdges() + kochBottom.getNrOfEdges();
    }
    
    public synchronized void addEdge(Edge e)
    {
        edges.add(e);
    }
    
    public synchronized void addCount()
    {
        counter++;
        if(counter==3)
        {
            ts2.setEnd();
            application.setTextCalc(ts2.toString());
            application.requestDrawEdges();
            counter = 0;
        }
    }

    public synchronized void drawEdges() {
        ts = new TimeStamp();
        ts.setBegin();
        application.clearKochPanel();
        for(Edge e : edges)
        {
            application.drawEdge(e);
        }
        ts.setEnd();
        application.setTextDraw(ts.toString());
    }

    private void startThreads() {
        tLeft.setName("Left");
        tRight.setName("Right");
        tBottom.setName("Bottom");
        tLeft.start();
        tRight.start();
        tBottom.start();
        
    }
}
