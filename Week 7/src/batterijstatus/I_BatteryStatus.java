package batterijstatus;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface I_BatteryStatus extends Library {
	public I_BatteryStatus INSTANCE=(I_BatteryStatus)Native.loadLibrary("Kernel32",I_BatteryStatus.class);
		
	// the function we need
	public int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);
}
