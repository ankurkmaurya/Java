package com.concept.tryfinally;

/**
 *
 * @author Ankur Maurya
 *
 */
public class TryFinally {

    public static void main(String[] args) {
        System.out.println("getVal main    : " + getVal());
        System.out.println();
        System.out.println("getValPlus main : " + getValPlus());
    }

    public static int getVal() {
        int i = 1;
        try {
            System.out.println("getVal try     : " + i);
            return i;
        } finally {
            i = 2;
            System.out.println("getVal finally : " + i);
        }
    }

    public static int getValPlus() {
        int i = 1;
        try {
            System.out.println("getValPlus try     : " + (i + 5));
            return i;
        } finally {
            i = 2;
            System.out.println("getValPlus finally : " + (i + 5));
        }
    }

}
