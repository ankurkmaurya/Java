package com.waitnotifythread;

/**
 *
 * @author Ankur Maurya
 *
 *
 */
public class A implements Runnable {

    private final Object lock;

    public A(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            for (int i = 48; i <= (48 + 9); i++) {
                synchronized (lock) {
                	lock.notify();
                    System.out.print((char) i);
                    lock.wait();
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
