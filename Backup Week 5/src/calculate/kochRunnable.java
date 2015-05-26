/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author jsf3
 */
public class kochRunnable implements Runnable,Observer
{
    KochFractal koch;
    private String side;
    private KochManager km;
    
    public kochRunnable(String side, KochManager km, KochFractal koch)
    {
        this.koch = koch;
        this.side = side;
        this.km = km;
        koch.addObserver(this);
    }
    
    @Override
    public synchronized void update(Observable o, Object arg) {
            km.addEdge((Edge) arg);
    }

    @Override
    public void run() {
        if(side.equals("Left"))
        {
            koch.generateLeftEdge();
            km.addCount();
        }
        else if(side.equals("Right"))
        {
            koch.generateRightEdge();
            km.addCount();
        }
        else if(side.equals("Bottom"))
        {
            koch.generateBottomEdge();
            km.addCount();
        }
    }
    
}
