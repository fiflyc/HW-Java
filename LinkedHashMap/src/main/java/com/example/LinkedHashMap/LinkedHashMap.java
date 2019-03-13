package com.example.LinkedHashMap;

import cucumber.api.java.ca.I;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;

public class LinkedHashMap<K, V> extends AbstractMap {

    static public class Set<E> {

        static private class Node<E> {
            private E value;
            private Node next;
            private Node prev;

            Node(@NotNull E value) {
                this.value = value;
                prev = null;
                next = null;
            }
        }

        private class Iterator implements java.util.Iterator<E> {

            private Set.Node<E> current;

            private Iterator(@Nullable Node<E> node) {
                current = node;
            }

            @Override
            public boolean hasNext() {
                return current == null;
            }

            @Override
            public E next() {
                return current.value;
            }
        }

        private Node<E> head;
        private Node<E> tail;
        private int size;

        private Set() {
            head = null;
            tail = null;
            size = 0;
        }

        private void put(@NotNull E value) {
            var node = new Node<>(value);
            size++;

            if (tail == null) {
                tail = node;
                head = node;
                return;
            }

            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        public Iterator iterator() {
            return new Iterator(head);
        }

        public int size() {
            return size;
        }
    }

    @Override
    public Object put(Object key, Object value) {

        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<Entry> entrySet() {
        return null;
    }
}