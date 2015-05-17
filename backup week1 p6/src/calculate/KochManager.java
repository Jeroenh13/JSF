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
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
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
    
    private final KochFractal kochLeft;
    private final KochFractal kochRight;
    private final KochFractal kochBottom;
    
    private Task taskLeft;
    private Task taskBottom;
    private Task taskRight;
    
    private int cnt = 0;
    
    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;
        kochLeft = new KochFractal();
        kochRight = new KochFractal();
        kochBottom = new KochFractal();
        edges = new ArrayList<>();
    }    

    public void changeLevel(int nxt){
        application.UpdateLeft(taskLeft);
        //application.UpdateBottom(taskBottom);
        //application.UpdateRight(taskRight);
        changeLevels(nxt);
        edges.clear();
        ts = new TimeStamp();
        ts.setBegin();
        ts2 = new TimeStamp();
        ts2.setBegin();
        
        taskLeft = new kochTask("Left",this, kochLeft);
        taskBottom = new kochTask("Bottom",this, kochRight);
        taskRight = new kochTask("Right",this, kochBottom);
        new Thread(taskLeft).start();
        new Thread(taskBottom).start();
        new Thread(taskRight).start();
     
         ts2.setEnd();
         application.setTextCalc(ts2.toString());
         application.requestDrawEdges();
         taskLeft.cancel();
    }
   
    public synchronized void add()
    {
        cnt++;
        if(cnt >=3){
            ts.setEnd();
            application.requestDrawEdges();
            cnt = 0;
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

    public synchronized void updateEdges(Edge e)
    {
        edges.add(e);
        Edge e1 = new Edge(e.X1,e.X2,e.Y1,e.Y2,Color.WHITE);
        application.drawOneEdge(e);
    }
}
