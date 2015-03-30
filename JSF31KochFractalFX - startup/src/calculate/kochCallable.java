/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author jsf3
 */
public class kochCallable implements Callable,Observer
{
    KochFractal koch;
    private String side;
    private KochManager km;
    private ArrayList<Edge> edges;
    
    public kochCallable(String side, KochManager km, KochFractal koch)
    {
        this.koch = koch;
        this.side = side;
        this.km = km;
        koch.addObserver(this);
        edges = new ArrayList<>();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);
    }

    @Override
    public Object call() throws Exception {
        if(side.equals("Left"))
        {
            koch.generateLeftEdge();
            km.Wait();
        }
        else if(side.equals("Right"))
        {
            koch.generateRightEdge();
            km.Wait();
        }
        else if(side.equals("Bottom"))
        {
            koch.generateBottomEdge();
            km.Wait();
        }
        return edges;
    }
    
}
