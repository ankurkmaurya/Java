package com.jointhread;

/**
 *
 * @author Ankur Maurya
 *
 */
public class B implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 65; i <= (65 + 9); i++) {
                System.out.print((char)i);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
