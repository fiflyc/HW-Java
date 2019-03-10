package com.example.Reflector.TestClasses;

import java.util.ArrayList;

public class ClassForComparisonC<E extends Object> {

    public final static int CONST = 100;
    private E field;

    static private void method1() {
    }
    public E method2() {
        return null;
    }
    public void method3(ArrayList<? extends Comparable> list) {
    }
}