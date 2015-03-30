/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    ExecutorService pool = Executors.newFixedThreadPool(4);
    
    Future<ArrayList<Edge>> fut;
    Future<ArrayList<Edge>> fut2;
    Future<ArrayList<Edge>> fut3;
        
    
    CyclicBarrier cb = new CyclicBarrier(4);
    
    kochCallable krl;
    kochCallable krr;
    kochCallable krb;
    
    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;
        kochLeft = new KochFractal();
        kochRight = new KochFractal();
        kochBottom = new KochFractal();
        edges = new ArrayList<>();
    }    

    public void changeLevel(int nxt){
        changeLevels(nxt);
        edges.clear();
        ts2 = new TimeStamp();
        ts2.setBegin();
        
        krl = new kochCallable("Left",this, kochLeft);
        krr = new kochCallable("Bottom",this, kochRight);
        krb = new kochCallable("Right",this, kochBottom);
        
        
        fut = pool.submit(krl);
        fut2 = pool.submit(krr);
        fut3 = pool.submit(krb);
        pool.execute(new Runnable(){
            @Override
            public void run()
            {
                try{
                    cb.await();
                    Thread.sleep(20);
                } catch (InterruptedException | BrokenBarrierException ex) {
                    Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                ts2.setEnd();
                application.setTextCalc(ts2.toString());
                application.requestDrawEdges();
            }
        });
    }
    
    public void Wait()
    {
        try {
            cb.await();
        } catch (InterruptedException | BrokenBarrierException ex ) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void drawEdges() {
        updateEdgeList();
        application.setTextNrEdges(String.valueOf(edges.size()));
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

    private void updateEdgeList() {
        try {
            edges.addAll(fut.get());
            edges.addAll(fut2.get());
            edges.addAll(fut3.get());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
