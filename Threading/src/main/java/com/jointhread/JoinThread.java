package com.jointhread;

/**
 *
 * @author Ankur Maurya
 *
 *  join() method is generally used to pause the execution of a current thread unless and until the specified thread on which join is called 
 *  is dead or completed. To stop a thread from running until another thread gets ended, this method can be used. 
 *  It joins the start of a thread execution to the end of another thread’s execution. 
 *  It is considered the final method of a thread class.
 *  When the join() method is invoked, the current thread stops its execution and the thread goes into the wait state. 
 *  The current thread remains in the wait state until the thread on which the join() method is invoked has achieved its dead state. 
 *  If interruption of the thread occurs, then it throws the InterruptedException.
 */
public class JoinThread {

    public static void main(String[] args) {
        try {
            Thread t1 = new Thread(new A());
            Thread t2 = new Thread(new B());
            Thread t3 = new Thread(new C());

            System.out.println("--> Execution Start");
            System.out.println("---- t1 Start");
            t1.start();
            t1.join();
            System.out.println("---- t1 End");
            System.out.println("---- t2 Start");
            t2.start();
            t2.join();
            System.out.println("---- t2 End");
            System.out.println("---- t3 Start");
            t3.start();
            t3.join();
            System.out.println("---- t3 End");
            System.out.println("--> Execution End");
        } catch (Exception e) {
            System.out.println("Exception main : " + e);
        }
    }

}
