package com.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * <li>b1B</li>
 * <li>C2c</li>
 * <li>dD3</li>
 * <li>4eE</li>
 * <li>F5f</li>
 * <li>g6G</li>
 * <li>Hh7</li>
 * <li>8Ii</li>
 * <li>j9J</li>
 *
 * <p>
 * The data is printed in all the three thread sequence in each and every line
 * but within the line the thread is scheduled in any order.</p>
 *
 *
 */
public class CyclicBarrierThreads {

    public static void main(String[] args) {
        try {
            CyclicBarrier barrier = new CyclicBarrier(3, () -> {
                System.out.println();
            });

            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(new A(barrier));
            executor.submit(new B(barrier));
            executor.submit(new C(barrier));
            executor.shutdown();
        } catch (Exception e) {
            System.out.println("Exception main : " + e);
        }
    }
}
