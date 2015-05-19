/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
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
    
    private KochFractal kochLeft;
    private KochFractal kochRight;
    private KochFractal kochBottom;
    
    private Task taskLeft;
    private Task taskBottom;
    private Task taskRight;
    
    private Thread t1;
    private Thread t2;
    private Thread t3;
    
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
        if(taskLeft != null)
        {
            taskLeft.cancel();
            taskRight.cancel();
            taskBottom.cancel();
        }
        edges.clear();
        
        kochLeft = new KochFractal();
        kochRight = new KochFractal();
        kochBottom = new KochFractal();
        
        changeLevels(nxt);
        
        cnt = 0;
        
        taskLeft = new kochTask("Left",this, kochLeft);
        taskBottom = new kochTask("Bottom",this, kochRight);
        taskRight = new kochTask("Right",this, kochBottom);
        
        application.UpdateLeft(taskLeft);
        application.UpdateBottom(taskBottom);
        application.UpdateRight(taskRight);
        
        ts2 = new TimeStamp();
        ts2.setBegin();
        
        new Thread(taskLeft,"ThLeft").start();
        new Thread(taskBottom,"ThBottom").start();
        new Thread(taskRight,"ThRight").start();
        
        ts = new TimeStamp();
        ts.setBegin();
        
         ts2.setEnd();
         application.setTextCalc(ts2.toString());
    }
   
    public synchronized void add()
    {
        cnt++;
        if(cnt >=3){
            ts.setEnd();
            application.requestDrawEdges();
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

    public synchronized void drawEdges() {
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
        Edge e1 = new Edge(e.X1,e.Y1,e.X2,e.Y2,Color.WHITE);
        application.drawOneEdge(e1);
    }
}
