package com.example.Reflector.TestClasses;

import java.util.ArrayList;
import java.util.function.Function;

public class ClassForComparisonB<E extends AbstractClass> {

    private E field;
    public final static float CONST = 100;

    private void method1() {
    }
    public E method2() {
        return null;
    }
    public void method3(ArrayList<? extends Function> list) {
    }
}