/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.concurrent.Task;

/**
 *
 * @author Jeroen
 */
public class kochTask extends Task<ArrayList<Edge>> implements Observer {

    KochFractal koch;
    private String side;
    private KochManager km;
    private ArrayList<Edge> edges;
    
    private int edgeCount = 0;
    
    public kochTask(String side, KochManager km, KochFractal koch)
    {
        this.koch = koch;
        this.side = side;
        this.km = km;
        koch.addObserver(this);
        edges = new ArrayList<>();
    }
    @Override
    protected ArrayList<Edge> call() throws Exception {
        if(side.equals("Left"))
        {
            koch.generateLeftEdge();
            km.add();
        }
        else if(side.equals("Right"))
        {
            koch.generateRightEdge();
            km.add();
        }
        else if(side.equals("Bottom"))
        {
            koch.generateBottomEdge();
            km.add();
        }
        return edges;
    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);
        edgeCount++;
        km.updateEdges((Edge)arg);
        try{
            Thread.sleep(20);
        }
        catch(InterruptedException ex){
            
        }
    }
    
}