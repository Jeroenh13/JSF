/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

import java.util.concurrent.locks.*;

/**
 *
 * @author Sebastiaan
 */
public class Monitor {

    private final Lock monLock = new ReentrantLock();
    private int writersActive;
    private int readersActive;
    private final Condition okToRead = monLock.newCondition();
    private final Condition okToWrite = monLock.newCondition();
    private int readersWaiting;
    private int writersWaiting;

    public void enterReader() {
        monLock.lock();
        try {
            while (writersActive != 0) {
                readersWaiting++;
                okToRead.await();
                readersWaiting--;
            }
            readersActive++;
        }catch(InterruptedException e){
            readersWaiting--;
            Thread.currentThread().interrupt();
        } 
        finally {
            monLock.unlock();
        }
    }

    public void exitReader() {
        monLock.lock();
        try {
            readersActive--;
            if (readersActive == 0) {
                okToWrite.signal();
            }
        } finally {
            monLock.unlock();
        }
    }

    public void enterWriter(){
        monLock.lock();
        try {
            while (writersActive > 0 || readersActive > 0) {
                writersWaiting++;
                okToWrite.await();
                writersWaiting--;
            }
            writersActive++;
        }catch(InterruptedException e){
            writersWaiting--;
            Thread.currentThread().interrupt();
        }  
        
        finally {
            monLock.unlock();
        }
    }

    public void exitWriter() {
        monLock.lock();
        try {
            writersActive--;
            if (writersWaiting > 0 &&  readersActive ==0) {
                okToWrite.signal();
            } else if(writersActive == 0 && readersWaiting > 0){
                okToRead.signalAll();
            }
        } finally {
            monLock.unlock();
        }
    }
}
