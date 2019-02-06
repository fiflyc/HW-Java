package com.example.SmartList;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SmartList<T> extends AbstractList<T> implements List<T> {

    private int size;

    private Object container;

    public SmartList() {
        size = 0;
        container = null;
    }

    public <C extends Collection<T>> SmartList(@NotNull C collection) {
        size = collection.size();
        if (size == 0) {
            container = null;
        } else if (size == 1) {
            container = collection.iterator().next();
        } else if (size < 6) {
            T array[] = (T[]) new Object[5];

            Iterator it = collection.iterator();
            int index = 0;
            while (it.hasNext()) {
                array[index] = (T) it.next();
                index++;
            }

            container = array;
        } else {
            container = new ArrayList<T>();

            Iterator it = collection.iterator();
            while (it.hasNext()) {
                ((ArrayList) container).add(it.next());
            }
        }
    }

    @Override
    public boolean add(@NotNull T element) {
        if (contains(element)) {
            return false;
        }

        if (size == 0) {
            container = element;
        } else if (size == 1) {
            T array[] = (T[]) new Object[5];
            array[0] = (T) container;
            array[1] = element;
        } else if (size < 5) {
            ((T[]) container)[size] = element;
        } else if (size == 5) {
            T array[] = (T[]) container;
            container = new ArrayList<T>();

            for (int i = 0; i < 6; i++) {
                ((ArrayList) container).add(array[i]);
            }
            ((ArrayList) container).add(element);
        } else {
            ((ArrayList) container).add(element);
        }

        size++;

        return true;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1) {
            return (T) container;
        } else if (size < 6) {
            return ((T[]) container)[index];
        } else {
            return (T) ((ArrayList) container).get(index);
        }
    }

    @Override
    public boolean contains(@NotNull Object element) {
        if (size == 0) {
            return false;
        } else if (size == 1) {
            return (T) container == element;
        } else if (size < 6) {
            for (int i = 0; i < size; i++) {
                if (((T[]) container)[i] == (T) element) {
                    return true;
                }
            }
            return false;
        } else {
            return ((ArrayList) container).contains(element);
        }
    }

    @Override
    public T remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        T result = null;

        if (size == 1) {
            result = (T) container;
            container = null;
        } else if (size < 6) {
            result = ((T[]) container)[index];
            for (int i = index; i < size; i++) {
                ((T[]) container)[i] = ((T[]) container)[i + 1];
            }
        } else {
            result = (T) ((ArrayList) container).remove(index);
        }

        size--;

        return result;
    }

    @Override
    public T set(int index, T element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        T result = get(index);

        if (size == 1) {
            container = element;
        } else if (size < 6) {
            ((T[]) container)[index] = element;
        } else {
            ((ArrayList) container).set(index, element);
        }

        return result;
    }

    @Override
    public int size() {
        return size;
    }
}