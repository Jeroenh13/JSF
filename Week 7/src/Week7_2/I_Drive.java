/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Week7_2;

import Week7_1.*;
import batterijstatus.I_BatteryStatus;
import batterijstatus.SYSTEM_POWER_STATUS;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import java.util.List;

/**
 *
 * @author Jeroen
 */
public interface I_Drive extends Library {

    public I_Drive INSTANCE = (I_Drive) Native.loadLibrary("Kernel32", I_Drive.class);
    
    public int GetDiskFreeSpaceA(Kernel32 result);

    public boolean GetDiskFreeSpaceA(String c, IntByReference r1, IntByReference r2, IntByReference r3, IntByReference r4);

}

