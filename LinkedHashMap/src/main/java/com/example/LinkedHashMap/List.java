package com.example.LinkedHashMap;

public class List<K, V> {

    /** Node of the List. */
    static private class Node<K, V> {
        private K key;
        private V value;
        private Node next;
        private Node prev;
    }

    /** The head of the List. */
    private Node head;

    /** Adds pair (key, value) at the tail of the List. */
    public void add(K key, V value) {
        Node newNode = new Node();
        newNode.key = key;
        newNode.value = value;
        newNode.next = head;
        if (head != null) {
            head.prev = newNode;
        }
        newNode.prev = null;
        head = newNode;
    }

    /**
     * Removes node by the key.
     * @return value of the removed Node or null if removed nothing
     */
    public Object remove(K key) {
        Node current = head;
        Object result = null;

        while (current != null) {
            if (current.key.equals(key)) {
                result = current.value;

                if (current.prev != null) {
                    current.prev.next = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                }

                if (current == head) {
                    head = current.next;
                }

                break;
            }

            current = current.next;
        }

        return result;
    }

    /** Returns the value corresponding to the key. */
    public Object find(String key) {
        Node current = head;
        Object result = null;

        while (current != null) {
            if (current.key.equals(key)) {
                result = current.value;
                break;
            }

            current = current.next;
        }

        return result;
    }

    /** Makes the List empty. */
    public void clear() {
        head = null;
    }
}