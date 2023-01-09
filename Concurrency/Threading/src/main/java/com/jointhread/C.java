package com.jointhread;

/**
 *
 * @author Ankur Maurya
 *
 */
public class C implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 97; i <= (97 + 9); i++) {
                System.out.print((char)i);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
