package com.waitnotifythread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Ankur Maurya
 * 
 * The wait() and notify() methods can be used to synchronize threads on
 * respective object lock. 
 * 
 * wait(): As the name suggests, it is a non-static
 * method that causes the current thread to wait and go to sleep until some
 * other threads call the notify () or notifyAll() method for the object’s
 * monitor (lock). 
 * It simply releases the lock and is mostly used for inter-thread communication. 
 * It is defined in the object class, and should only be called from a synchronized context.
 * 
 * notify() : It sends a notification and wakes up only a single thread instead
 * of multiple threads that are waiting on the object’s monitor. 
 * notifyAll(): It sends notifications and wakes up all threads and allows 
 * them to compete for the object's monitor instead of a single thread.
 * 
 */
public class WaitNotifyThread {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(new A(lock));
            executor.submit(new B(lock));
            executor.shutdown();
        } catch (Exception e) {
            System.out.println("Exception main : " + e);
        }
    }

}
