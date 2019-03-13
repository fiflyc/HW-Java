package com.example.LinkedHashMap;

class List<K, V> {

    /** Node of the List. */
    static class Node<K, V> {
        private K key;
        private V value;
        private Node next;
        private Node prev;
        private Node nextByOrder;
        private Node prevByOrder;

        Node<K, V> nextByOrder() {
            return nextByOrder;
        }

        K getKey() {
            return key;
        }

        V getValue() {
            return value;
        }
    }

    /** The head of the List. */
    private Node head;

    /** Adds pair (key, value) at the tail of the List. */
    public Node add(K key, V value, Node prevByOrder) {
        Node newNode = new Node();
        newNode.key = key;
        newNode.value = value;
        newNode.next = head;

        newNode.prevByOrder = prevByOrder;
        newNode.nextByOrder = null;
        if (prevByOrder != null) {
            prevByOrder.nextByOrder = newNode;
        }

        if (head != null) {
            head.prev = newNode;
        }

        newNode.prev = null;
        head = newNode;

        return newNode;
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
                if (current.nextByOrder != null) {
                    current.nextByOrder.prevByOrder = current.prevByOrder;
                }
                if (current.prevByOrder != null) {
                    current.prevByOrder.nextByOrder = current.nextByOrder;
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
    public Object find(K key) {
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