package com.cyclicbarrierthreadsequence;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ankur Maurya
 *
 * <p>
 * <b>CyclicBarrier:</b> It is a tool to synchronize threads processing using
 * some algorithm. It enables a set of threads to wait for each other till they
 * reach a common execution point or common barrier points, and then let them
 * further continue execution. One can reuse the same CyclicBarrier even if the
 * barrier is broken by setting it.</p>
 *
 * <p>
 * This class will print the data as</p>
 * <li>0Aa</li>
 * <li>1Bb</li>
 * <li>2Cc</li>
 * <li>3Dd</li>
 * <li>4Ee</li>
 * <li>5Ff</li>
 * <li>6Gg</li>
 * <li>7Hh</li>
 * <li>8Ii</li>
 * <li>9Jj</li>
 *
 *
 */
public class CyclicBarrierSequenceThreads {
    
    public static void main(String[] args) {
        try {
            CyclicBarrier barrier = new CyclicBarrier(3, System.out::println);
            
            AtomicInteger seq = new AtomicInteger();
            seq.set(1);

            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(new A(barrier, seq));
            executor.submit(new B(barrier, seq));
            executor.submit(new C(barrier, seq));
            executor.shutdown();     
        } catch (Exception e) {
            System.out.println("Exception main : " + e);
        }
    }
}
