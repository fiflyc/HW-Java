package com.example.Reflector.TestClasses;

public class GenericClass<T extends AbstractClass> {

    private T field;

    public void setField(T value) {
        field = value;
    }

    public T getField() {
        return field;
    }
}