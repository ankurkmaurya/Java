package com.ankurmaurya.inheritance.parentclassprivatemethod;

/**
 *
 * @author Ankur.Mourya
 *
 */
public class Animal {

    private void m1() {
        System.out.println("Animal Private method m1()");
    }

    public void m2() {
        System.out.println("Animal Public method m2()");
        m1(); //call to class private method
    }

}
