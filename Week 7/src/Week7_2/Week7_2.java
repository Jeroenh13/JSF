/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Week7_2;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author Jeroen
 */
public class Week7_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        I_Drive kernel32 = I_Drive.INSTANCE;
        IntByReference r1 = new IntByReference(), r2 = new IntByReference(),
                r3 = new IntByReference(), r4 = new IntByReference();
        if (kernel32.GetDiskFreeSpaceA("C:\\", r1, r2, r3, r4)) {
            long multiplier = r1.getValue() * r2.getValue();
            long free = r3.getValue() * multiplier;
            long total = r4.getValue() * multiplier;
            System.out.printf("'C:\\': free: %d, total: %d %n", free, total);
        } else {
            System.out.printf("GetDiskFreeSpaceEx() returned false%n");
        }
        System.out.printf("All done%n");
    }

}
