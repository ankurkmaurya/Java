package com.jointhread;

/**
 *
 * @author Ankur Maurya
 *
 */
public class A implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 48; i <= (48 + 9); i++) {
                System.out.print((char)i);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
