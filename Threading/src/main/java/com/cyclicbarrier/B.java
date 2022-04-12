package com.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Ankur Maurya
 *
 */
public class B implements Runnable {

    private final CyclicBarrier barrier;

    public B(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            for (int i = 65; i <= (65 + 9); i++) {
                System.out.print((char)i);
                barrier.await();
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

}
