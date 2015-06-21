/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helloC;

import com.sun.jna.Native;

/**
 *
 * @author erik connect the JAVA interface to the right C library Wndows
 * version: msvcrt.dll
 *
 */
public interface IHello_Win extends CLibrary {

    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("msvcrt", CLibrary.class);
}
