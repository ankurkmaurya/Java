package com.waitnotifythread;

/**
 *
 * @author Ankur Maurya
 *
 */
public class B implements Runnable {

    private final Object lock;

    public B(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            for (int i = 65; i <= (65 + 9); i++) {
                synchronized (lock) {
                	lock.notify();
                    System.out.println((char) i);
                    lock.wait();
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
