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
public class kochTask extends Task<Void> implements Observer {

    KochFractal koch;
    private String side;
    private KochManager km;
    private int edgeCount;
    private int sleepTime;

    public kochTask(String side, KochManager km, KochFractal koch) {
        this.koch = koch;
        this.side = side;
        this.km = km;
        edgeCount = 0;
    }

    @Override
    protected Void call() {
        koch.addObserver(this);
        if (side.equals("Left")) {
            sleepTime = 5;
            koch.generateLeftEdge();
            km.add();
        } else if (side.equals("Right")) {
            sleepTime = 20;
            koch.generateRightEdge();
            km.add();
        } else if (side.equals("Bottom")) {
            sleepTime = 15;
            koch.generateBottomEdge();
            km.add();
        }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (this.isCancelled()) {
            koch.cancel();
            return;
        }

        edgeCount++;
        km.updateEdges((Edge) arg);
        updateProgress(edgeCount, koch.getNrOfEdges() / 3);
        updateMessage("Number of edges: " + Integer.toString(edgeCount));
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            if (isCancelled()) {
                koch.cancel();
            }
        }

    }
}
