/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Week7_1;

import batterijstatus.SYSTEM_POWER_STATUS;
import java.io.Console;

/**
 *
 * @author Jeroen
 */
public class Week7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        I_Time lib = I_Time.INSTANCE;

        SYSTEMTIME time = new SYSTEMTIME();

        lib.GetSystemTime(time);

        System.out.println("Before: " + time.wMilliseconds);
        for (int i = 0; i < 1000000000; i++);

        lib.GetSystemTime(time);
        System.out.println("After:  " + time.wMilliseconds);

        System.out.println("Before: " + Long.toString(System.nanoTime()));
        for (int i = 0; i < 1000000000; i++);
        System.out.println("After:  " + Long.toString(System.nanoTime()));
    }

}
