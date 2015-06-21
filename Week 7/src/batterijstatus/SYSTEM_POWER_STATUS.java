package batterijstatus;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

// create class for struct needed by native function
//
/*
 * From the API provide by Microsoft (http://msdn2.microsoft.com/en-us/library/aa373232.aspx)
 * 	typedef struct _SYSTEM_POWER_STATUS {
 *		BYTE ACLineStatus;
 *		BYTE BatteryFlag;
 *		BYTE BatteryLifePercent;
 *		BYTE Reserved1;
 *		DWORD BatteryLifeTime;
 *		DWORD BatteryFullLifeTime;
 *	} SYSTEM_POWER_STATUS, *LPSYSTEM_POWER_STATUS;
 *
 *  Een struct met simpele datatypes werkt goed in JNA.
 *
 */
public class SYSTEM_POWER_STATUS extends Structure {

    public byte ACLineStatus;
    public byte BatteryFlag;
    public byte BatteryLifePercent;
    public byte Reserved1;
    public int BatteryLifeTime;
    public int BatteryFullLifeTime;

    /*
     * The AC power status
     */
    public String getACLineStatusString() {
        String returnValue = "Unknown";
        switch (this.ACLineStatus) {
            case (0):
                returnValue = "Offline";
                break;
            case (1):
                returnValue = "Online";
                break;
        }
        return returnValue;
    }

    /*
     * The battery charge status
     */
    public String getBatteryFlagString() {
        String returnValue = "";
        switch (this.BatteryFlag) {
            case (1):
                returnValue = "High: the battery capacity is at more than 66 percent";
                break;
            case (2):
                returnValue = "Low: the battery capacity is at less than 33 percent";
                break;
            case (4):
                returnValue = "Critical: the battery capacity is at less than five percent";
                break;
            case (8):
                returnValue = "Charging";
                break;
            case ((byte) 128):
                returnValue = "No system battery";
                break;
        }
        return returnValue;
    }

    /*
     * The percentage of full battery charge remaining
     */
    public String getBatteryLifePercent() {
        String returnValue;
        if (this.BatteryLifePercent == (byte) 255) {
            returnValue = "Unknown";
        } else {
            returnValue = this.BatteryLifePercent + "%";
        }

        return returnValue;
    }

    /*
     * The number of seconds of battery life remaining
     */
    public String getBatteryLifeTime() {
        String returnValue;
        if (this.BatteryLifeTime == -1) {
            returnValue = "Unknown";
        } else {
            returnValue = this.BatteryLifeTime + " seconds";
        }
        return returnValue;
    }

    /*
     * The number of seconds of battery life when at full charge
     */
    public String getBatteryFullLifeTime() {
        String returnValue;
        if (this.BatteryFullLifeTime == -1) {
            returnValue = "Unknown";
        } else {
            returnValue = this.BatteryFullLifeTime + " seconds";
        }
        return returnValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ACLineStatus: " + this.getACLineStatusString() + "\n");
        sb.append("Battery Flag: " + this.getBatteryFlagString() + "\n");
        sb.append("Battery Life: " + this.getBatteryLifePercent() + "\n");
        sb.append("Battery Left: " + this.getBatteryLifeTime() + "\n");
        sb.append("Battery Full: " + this.getBatteryFullLifeTime() + "\n");
        return sb.toString();
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{"ACLineStatus", "BatteryFlag", "BatteryLifePercent", "Reserved1", "BatteryLifeTime", "BatteryFullLifeTime"});
    }
}
