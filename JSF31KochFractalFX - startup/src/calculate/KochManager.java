/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    CyclicBarrier cb = new CyclicBarrier(3);
    
    kochRunnable krl;
    kochRunnable krr;
    kochRunnable krb;
    
    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;
        kochLeft = new KochFractal();
        kochRight = new KochFractal();
        kochBottom = new KochFractal();
        edges = new ArrayList<>();
    }    

    public synchronized void changeLevel(int nxt){
        changeLevels(nxt);
        edges.clear();
        ts2 = new TimeStamp();
        ts2.setBegin();
        
        //create runnables and pool
        krl = new kochRunnable("Left",this, kochLeft,cb);
        krb = new kochRunnable("Bottom",this, kochRight,cb);
        krr = new kochRunnable("Right",this, kochBottom,cb);
        ExecutorService pool = Executors.newFixedThreadPool(3);
        
        
        Future<ArrayList<Edge>> fut = pool.submit(krl);
        Future<ArrayList<Edge>> fut2 = pool.submit(krb);
        Future<ArrayList<Edge>> fut3 = pool.submit(krr);
        
        try {
            edges.addAll(fut.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            edges.addAll(fut2.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            edges.addAll(fut3.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pool.shutdown();
        ts2.setEnd();
        application.setTextCalc(ts2.toString());
        application.setTextNrEdges(String.valueOf(getEdges()));
        application.requestDrawEdges();
    }
    
    void changeLevels(int nxt)
    {
        kochLeft.setLevel(nxt);
        kochRight.setLevel(nxt);
        kochBottom.setLevel(nxt);
    }
    
    
    int getEdges()
    {
        return kochLeft.getNrOfEdges();
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
}
