/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloC;

/**
 *
 * @author marcel
 */
import com.sun.jna.Native;

/**
 *
 * @author erik
 *
 * connect the JAVA interface to the right C library Linux version: c.so
 */
public interface IHello_Mac extends CLibrary {

    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
}