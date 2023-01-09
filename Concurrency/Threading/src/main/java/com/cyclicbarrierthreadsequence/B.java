package com.cyclicbarrierthreadsequence;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ankur Maurya
 *
 */
public class B implements Runnable {

    private final CyclicBarrier barrier;
    private final AtomicInteger seq;

    public B(CyclicBarrier barrier, AtomicInteger seq) {
        this.barrier = barrier;
        this.seq = seq;
    }

    @Override
    public void run() {
        try {
            for (int i = 65; i <= (65 + 9); i++) {
                while (seq.get() != 2) {
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
