/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helloC;

import com.sun.jna.Library;

/**
 *
 * @author erik
 * 
 * determine which method/functions calls are used from JAVA to native library
 */
public interface CLibrary extends Library {

    void printf(String format, Object... args);
}
