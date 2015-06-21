/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helloC;

import com.sun.jna.Platform;

/**
 *
 * @author erik
 *
 * This class uses a call to a native C method/function. In this example: printf
 *
 */
public class HelloWorld {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //
        // Get the right instance of the C library, depending on the
        // platform you are on.
        CLibrary clib = null;
        switch (Platform.getOSType()) {
            case Platform.WINDOWS:
                clib = IHello_Win.INSTANCE;
                break;
            case Platform.LINUX:
                clib = IHello_Lin.INSTANCE;
                break;
            case Platform.MAC:
                clib = IHello_Mac.INSTANCE;
                break;
            default:
                clib = null;
        }

        // roep printf aan met de juiste library
        if (clib != null) {
            clib.printf("Hello, World\n");
            for (int i = 0; i < args.length; i++) {
                clib.printf("Argument %d: %s\n", i, args[i]);
            }
        } else {
            System.out.println("call to native library impossible");
        }
    }
}
