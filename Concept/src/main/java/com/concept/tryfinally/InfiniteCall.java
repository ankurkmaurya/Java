
package com.concept.tryfinally;

/**
 * 
 *  Each time it reaches the maximum stack depth it throws an exception and unwinds the stack. 
 *  However in the finally it calls Foo again causing it to again reuse the stack space it has just recovered. 
 *  It will go back and forth throwing exceptions and then going back Down the stack until it happens again forever.
 * 
 */
public class InfiniteCall {
  
    public static void main(String[] args) {
        foo(1); 
    }
    
    public static void foo(int x) {
        System.out.println("foo - " + x);
        try {
            foo(x+1);
        } finally {
            System.out.println("finally - " + x);
            foo(x+1);
        }
    }
           
    
    
}
