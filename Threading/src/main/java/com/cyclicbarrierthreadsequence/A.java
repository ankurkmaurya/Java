package com.cyclicbarrierthreadsequence;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ankur Maurya
 *
 */
public class A implements Runnable {

    private final CyclicBarrier barrier;
    private final AtomicInteger seq;

    public A(CyclicBarrier barrier, AtomicInteger seq) {
        this.barrier = barrier;
        this.seq = seq;
    }

    @Override
    public void run() {
        try {
            for (int i = 48; i <= 48 + 9; i++) {
                while (seq.get() != 1) {
                    //loop continuously  
                }
                System.out.print((char) i);
                seq.incrementAndGet();
                barrier.await();
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

}
