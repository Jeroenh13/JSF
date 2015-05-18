/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
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
        tryCancel();
        edges.clear();
        
        cnt = 0;
        taskLeft = new kochTask("Left",this, kochLeft);
        taskBottom = new kochTask("Bottom",this, kochRight);
        taskRight = new kochTask("Right",this, kochBottom);
        
        application.UpdateLeft(taskLeft);
        application.UpdateBottom(taskBottom);
        application.UpdateRight(taskRight);
        
        ts2 = new TimeStamp();
        ts2.setBegin();
        
        new Thread(taskLeft).start();
        new Thread(taskBottom).start();
        new Thread(taskRight).start();
        
        changeLevels(nxt);
        
        ts = new TimeStamp();
        ts.setBegin();
        
         ts2.setEnd();
         application.setTextCalc(ts2.toString());
         application.requestDrawEdges();
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

    public void tryCancel() {
        try{
            taskLeft.cancel();
            taskRight.cancel();
            taskBottom.cancel();
            taskLeft = null;
            taskRight = null;
            taskBottom = null;
        }
        catch(NullPointerException  ex){
        }
    }
}
