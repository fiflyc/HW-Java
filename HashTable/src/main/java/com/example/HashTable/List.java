package com.example.HashTable;

import java.util.ArrayList;

/** Simple realization of the List, containing pairs (String, String). */
public class List {

    /** Node of the List. */
    private class Node {
        private String key;
        private String value;
        private Node next;
        private Node prev;
    }

    /** The head of the List. */
    private Node head;

    /** Adds pair (key, value) at the tail of the List. */
    public void add(String key, String value) {
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
    public String remove(String key) {
        Node current = head;
        String result = null;

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
    public String find(String key) {
        Node current = head;
        String result = null;

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

    /** Returns list of all keys in a List. */
    public ArrayList<String> getAllKeys() {
        Node current = head;
        var result = new ArrayList<String>();

        while (current != null) {
            result.add(current.key);
            current = current.next;
        }

        return result;
    }
}