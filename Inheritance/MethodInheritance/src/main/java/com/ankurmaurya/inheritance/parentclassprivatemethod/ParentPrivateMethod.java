package com.ankurmaurya.inheritance.parentclassprivatemethod;

/**
 * 
 * We can hide super class private method in child class as private methods are
 * not inherited but if a public method in superclass calls its private method
 * in the same class and if child class extends the parent class then that child
 * class can call the public method of the parent class which in turn can call
 * the private method. 
 * 
 * So the private method of parent class can be accessible if any public 
 * method of parent class will call its private method in the same class.
 *
 *
 * @author Ankur.Mourya
 * 
 * 
 * OUTPUT :
 * Bird Public method m3()
 * Animal Public method m2()
 * Animal Private method m1()
 *
 */
public class ParentPrivateMethod {

    public static void main(String[] args) {
        Bird b = new Bird();
        b.m3();
    }

}
