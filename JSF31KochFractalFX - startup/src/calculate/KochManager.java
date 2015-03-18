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
public class KochManager implements Observer
{
    private JSF31KochFractalFX application;
    private KochFractal koch;
    private ArrayList<Edge> edges;
    private TimeStamp ts;
    private TimeStamp ts2;
    
    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;
        koch = new KochFractal();
        koch.addObserver(this);
        edges = new ArrayList<Edge>();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge)arg);
    }
    

    public void changeLevel(int nxt) {
        koch.setLevel(nxt);
        edges.clear();
        ts2 = new TimeStamp();
        ts2.setBegin();
        koch.generateBottomEdge();
        koch.generateLeftEdge();
        koch.generateRightEdge();
        ts2.setEnd();
        drawEdges();
        application.setTextNrEdges(String.valueOf(edges.size()));
        application.setTextCalc(ts2.toString());
    }

    public void drawEdges() {
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
