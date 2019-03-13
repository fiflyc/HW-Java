package com.example.LinkedHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Map;

public class LinkedHashMap<K, V> extends AbstractMap {

    static public class Entry<K, V> implements Map.Entry {

        private K key;
        private V value;

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            this.value = (V) value;
            return value;
        }
    }

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

    public class Set<E> extends java.util.AbstractSet {

        private class Iterator implements java.util.Iterator<E> {

            private Node<E> current;

            private Iterator(@Nullable Node<E> node) {
                current = node;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                var result = current.value;
                current = current.next;
                return result;
            }
        }

        private Node<E> head;
        private Node<E> tail;
        private int size;

        private Set() {
            head = null;
            size = 0;
        }

        private void put(@NotNull E value) {
            var node = new Node<>(value);
            size++;

            if (head == null) {
                head = node;
                tail = node;
                return;
            }

            head.next = node;
            node.prev = head;
            head = node;
        }

        @Override
        public Iterator iterator() {
            return new Iterator(tail);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            head = null;
        }
    }

    private List[] container;
    private int size;
    private List.Node<K, V> firstByOrder;
    private List.Node<K, V> lastByOrder;
    private static final int DEFAULT_CONTAINER_LENGTH = 10000;

    LinkedHashMap() {
        this(DEFAULT_CONTAINER_LENGTH);
    }

    LinkedHashMap(int capacity) {
        if (capacity > 0) {
            container = new List[capacity];
        } else {
            container = new List[DEFAULT_CONTAINER_LENGTH];
        }

        for (int i = 0; i < container.length; i++) {
            container[i] = new List<K, V>();
        }

        firstByOrder = null;
        lastByOrder = null;
        size = 0;

    }

    @Override
    public Object put(@NotNull Object key, @NotNull Object value) {
        Object result = container[getHash((K) key)].find(key);
        if (result != null) {
            container[getHash((K) key)].remove(key);
        } else {
            size++;
        }

        lastByOrder = container[getHash((K) key)].add(key, value, lastByOrder);
        if (firstByOrder == null) {
            firstByOrder = lastByOrder;
        }

        return result;
    }

    @Override
    public V remove(@NotNull Object key) {
        if (key.equals(firstByOrder.getKey())) {
            firstByOrder = firstByOrder.nextByOrder();
        }

        Object result = container[getHash((K) key)].find(key);
        if (result != null) {
            size--;
        }

        container[getHash((K) key)].remove(key);

        return (V) result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public java.util.Set<Entry<K, V>> entrySet() {
        var set = new Set<Entry<K, V>>();
        for (List.Node<K, V> current = firstByOrder; current != null; current = current.nextByOrder()) {
            Entry<K, V> entry = new Entry<K, V>();
            entry.value = current.getValue();
            entry.key = current.getKey();
            set.put(entry);
        }

        return set;
    }

    private int getHash(@NotNull K key) {
        return key.hashCode() % container.length;
    }
}