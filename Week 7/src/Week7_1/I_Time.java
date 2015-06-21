/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Week7_1;

import batterijstatus.I_BatteryStatus;
import batterijstatus.SYSTEM_POWER_STATUS;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import java.util.List;

/**
 *
 * @author Jeroen
 */
public interface I_Time extends Library {

    public I_Time INSTANCE = (I_Time) Native.loadLibrary("Kernel32", I_Time.class);
    
    public int GetSystemTime(SYSTEMTIME result);

}

