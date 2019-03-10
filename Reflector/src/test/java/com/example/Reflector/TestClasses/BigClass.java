package com.example.Reflector.TestClasses;

import java.util.ArrayList;

public class BigClass<T> extends Parent implements Interface1, Interface2 {

    private static interface Interface3 {
    }

    private static class Nested<T> {

        Nested<T> field1;
        Nested<T> field2;
    }

    private class Inner implements Interface3 {

        private T field;

        Inner() {
            field = null;
        }
        void set(T value) {
        }
        T get() {
            return field;
        }
    }

    private static String field1;
    private ArrayList<T> field2;
    private final int CONST = 10;

    public BigClass(String string, ArrayList<T> list) {
    }
    public void print() {
    }
    public T getFirst() {
        return field2.get(0);
    }
    public void removeFirst() {
    }
}