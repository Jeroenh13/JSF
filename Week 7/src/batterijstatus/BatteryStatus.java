package batterijstatus;

import java.io.Console;

/**
 * JNA voorbeeld voor opvragen batterijgebruik in laptops.
 *
 * @author Admin
 *
 */
public class BatteryStatus {

    public static void main(String[] args) {
        I_BatteryStatus lib = I_BatteryStatus.INSTANCE;

        SYSTEM_POWER_STATUS batteryStatus = new SYSTEM_POWER_STATUS();

        lib.GetSystemPowerStatus(batteryStatus);

        System.out.println(batteryStatus);
        Console cons = System.console();
        if (cons != null) { // when run from console
            cons.printf("Enter to exit ");
            cons.readLine();
        }

    }
}
