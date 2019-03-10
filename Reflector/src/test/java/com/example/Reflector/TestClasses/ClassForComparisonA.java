package com.example.Reflector.TestClasses;

import java.util.ArrayList;

public class ClassForComparisonA<E> {

    public static final int CONST = 100;
    private E field;

    private static void method1() {
    }
    public E method2() {
        return null;
    }
    public void method3(ArrayList<? extends Comparable> list) {
    }
}