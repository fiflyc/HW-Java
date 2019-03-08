package com.example.Reflector.TestClasses;

public class GenericClass<T> {

    private T field;

    public void setField(T value) {
        field = value;
    }

    public T getField() {
        return field;
    }
}