package com.cyclicbarrierthreadsequence;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ankur Maurya
 *
 */
public class C implements Runnable {

    private final CyclicBarrier barrier;
    private final AtomicInteger seq;

    public C(CyclicBarrier barrier, AtomicInteger seq) {
        this.barrier = barrier;
        this.seq = seq;
    }

    @Override
    public void run() {
        try {
            for (int i = 97; i <= (97 + 9); i++) {
                while (seq.get() != 3) {
                    //loop continuously  
                }
                System.out.print((char) i);
                seq.set(1);
                barrier.await();
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

}
