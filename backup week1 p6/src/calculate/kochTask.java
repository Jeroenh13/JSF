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
    private int edgeCount;
    private int sleepTime;
    
    public kochTask(String side, KochManager km, KochFractal koch)
    {
        this.koch = koch;
        this.side = side;
        this.km = km;
        koch.addObserver(this);
        edgeCount = 0;
    }
    
    @Override
    protected ArrayList<Edge> call() throws Exception {
        if(side.equals("Left"))
        {
            koch.generateLeftEdge();
            km.add();
            sleepTime = 5;
        }
        else if(side.equals("Right"))
        {
            koch.generateRightEdge();
            km.add();
            sleepTime = 20;
        }
        else if(side.equals("Bottom"))
        {
            koch.generateBottomEdge();
            km.add();
            sleepTime = 15;
        }
        return null;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if(isCancelled())
        {
            koch.cancel();
        }
        else{
            edgeCount++;
            km.updateEdges((Edge)arg);
            updateProgress(edgeCount,koch.getNrOfEdges()/3);
            updateMessage("Number of edges: " + Integer.toString(edgeCount));
            try{
                Thread.sleep(sleepTime);
            }
            catch(InterruptedException ex){
            }
        }
    }
}