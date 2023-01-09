package com.ankurmaurya.inheritance.parentclassprivatemethod;

/**
 *
 * @author Ankur.Mourya
 * 
 */
public class Bird extends Animal {

    public void m3() {
        System.out.println("Bird Public method m3()");
        m2(); //Call to parent class public method
    }
}
