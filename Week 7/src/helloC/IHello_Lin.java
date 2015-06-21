/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helloC;

import com.sun.jna.Native;

/**
 *
 * @author erik
 *
 * connect the JAVA interface to the right C library Linux version: c.so
 */
public interface IHello_Lin extends CLibrary {

    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
}
