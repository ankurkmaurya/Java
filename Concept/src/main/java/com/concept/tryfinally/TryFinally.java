package com.concept.tryfinally;

/**
 *
 * @author Ankur Maurya
 * 
 * 
 *
 */
public class TryFinally {

    public static void main(String[] args) {
        System.out.println("-- Return from try");
        System.out.println("getVal main    : " + getVal());
        System.out.println();
        System.out.println("-- Return from try");
        System.out.println("getValPlus main : " + getValPlus());
        System.out.println();
        System.out.println("-- Return from finally");
        System.out.println("getValFinally main : " + getValFinally());
        System.out.println();
        System.out.println("-- finally break");
        System.out.println("finallyBreak : " + finallyBreak(true));
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
    
    public static int getValFinally() {
        int i = 1;
        try {
            System.out.println("getValFinally try     : " + i);
            return i;
        } finally {
            i = 2;
            System.out.println("getValFinally finally : " + i);
            return i;
        }
    }
    
    public static boolean finallyBreak(boolean bVal) {
        while (bVal) {
            try {
                return true;
            }
            finally {
                break;
            }
        }
        return false;
    }
    
    

}
