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
public class kochRunnable implements Callable,Observer
{
    KochFractal koch;
    private String side;
    private KochManager km;
    private ArrayList<Edge> edges;
    CyclicBarrier cb;
    
    public kochRunnable(String side, KochManager km, KochFractal koch,CyclicBarrier cb)
    {
        this.koch = koch;
        this.side = side;
        this.cb = cb;
        this.km = km;
        koch.addObserver(this);
        edges = new ArrayList<>();
    }
    
    @Override
    public synchronized void update(Observable o, Object arg) {
        edges.add((Edge) arg);
    }

    @Override
    public Object call() throws Exception {
        if(side.equals("Left"))
        {
            koch.generateLeftEdge();
            cb.await();
        }
        else if(side.equals("Right"))
        {
            koch.generateRightEdge();
            cb.await();
        }
        else if(side.equals("Bottom"))
        {
            koch.generateBottomEdge();
            cb.await();
        }
        return edges;
    }
    
}
