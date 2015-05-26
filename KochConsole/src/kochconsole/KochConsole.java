/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochconsole;

import calculate.*;

/**
 *
 * @author jsf3
 */
public class KochConsole {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KochFractal kochFractal = new KochFractal();
        KochObserver kobserver = new KochObserver();
        kochFractal.addObserver(kobserver);
        
        kochFractal.setLevel(1);
        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
    }
}
