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
        ts = new TimeStamp();
        ts.setBegin();
        koch.setLevel(nxt);
        drawEdges();
        ts.setEnd();
        application.setTextCalc(ts.toString());
        application.setTextNrEdges(String.valueOf(edges.size()));
    }

    public void drawEdges() {
        edges.clear();
        application.clearKochPanel();
        koch.generateBottomEdge();
        koch.generateLeftEdge();
        koch.generateRightEdge();
        for(Edge e : edges)
        {
            application.drawEdge(e);
        }
    }
}
