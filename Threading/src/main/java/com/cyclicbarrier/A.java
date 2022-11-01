package com.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Ankur Maurya
 *
 */
public class A implements Runnable {

    private final CyclicBarrier barrier;

    public A(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            for (int i = 48; i <= (48 + 9); i++) {
                System.out.print((char)i);
                barrier.await();
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

}
