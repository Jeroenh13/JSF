/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Week7_2;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import java.util.List;

/**
 *
 * @author Jeroen
 */
public class Kernel32 extends Structure{

    @Override
    protected List getFieldOrder() {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean GetDiskFreeSpaceA(String c, IntByReference r1, IntByReference r2, IntByReference r3, IntByReference r4) {
        return false;
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
