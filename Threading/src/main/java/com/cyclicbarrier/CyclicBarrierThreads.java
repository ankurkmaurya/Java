package com.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Ankur Maurya
 *
 * CyclicBarrier: It is a tool to synchronize threads processing using some
 * algorithm. It enables a set of threads to wait for each other till they reach
 * a common execution point or common barrier points, and then let them further
 * continue execution. One can reuse the same CyclicBarrier even if the barrier
 * is broken by setting it.
 *
 * This class will print the data as
 *  0Aa
 *  b1B
 *  C2c
 *  dD3
 *  4eE
 *  F5f
 *  g6G
 *  Hh7
 *  8Ii
 *  j9J
 * 
 *  The data is printed in all the three thread sequence in each and every line but within the line
 *  the thread is scheduled in any order.
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
