package com.example.Trie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Trie (http://neerc.ifmo.ru/wiki/index.php?title=%D0%91%D0%BE%D1%80).
 * Implements interface Serializable.
 * */
public class Trie implements Serializable {

    /** A node of a trie */
    private class Node {

        /** A HashMap contains references to the next nodes. */
        private HashMap<Character, Node> next;

        /** A number of elements passing through this node. */
        int weight;

        private boolean isTerminal;
    }

    /** A root of a trie. */
    private Node root;

    /** A size of a trie. */
    private int size;

    Trie() {
        root = null;
        size = 0;
    }

    /**
     * Adds string into a trie.
     * @param element -- string added in trie
     * @throws IllegalArgumentException if an element is null
     * @return true if element wasn't in trie before adding
     */
    public boolean add(@NotNull String element) {
        if (root == null) {
            root = createNewBranch(element);
            size++;
            return true;
        }

        Node current = root;
        Node next;
        boolean result = false;

        for (int i = 0; i < element.length(); i++) {
            next = current.next.get(element.charAt(i));

            if (next != null) {
                current = next;
            } else {
                next = createNewBranch(element.substring(i + 1));
                current.next.put(element.charAt(i), next);
                result = true;
                break;
            }
        }

        if (result) {
            size++;
        }

        return result;
    }

    /**
     * Checks an availability of an element.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element is in a trie
     */
    public boolean contains(@NotNull String element) {
        Node current = root;
        Node next;

        for (char symb: element.toCharArray()) {
            next = current.next.get(symb);

            if (next != null) {
                current = next;
            } else {
                return false;
            }
        }

        return current.isTerminal;
    }

    /**
     * Removes an element from a trie.
     * @param element
     * @throws IllegalArgumentException if an element is null
     * @return true if an element was in a trie before removing
     */
    public boolean remove(@NotNull String element) {
        if (!contains(element)) {
            return false;
        }

        Node current = root;
        current.weight--;
        Node next;

        for (char symb: element.toCharArray()) {
            next = current.next.get(symb);

            if (next.weight > 1) {
                next.weight--;
                current = next;
            } else {
                current.next.remove(symb);
                return true;
            }
        }
        current.isTerminal = false;

        return true;
    }

    /**
     * Returns a size of a trie.
     * @return number of containing strings
     */
    public int size() {
        return size;
    }

    /**
     * Returns the number of strings have a matching prefix.
     * @param prefix
     * @throws IllegalArgumentException if a prefix is null
     * @return the number of strings starts with a prefix
     */
    public int howManyStartsWithPrefix(@NotNull String prefix) {
        Node current = root;
        Node next;

        for (char symb: prefix.toCharArray()) {
            next = current.next.get(symb);

            if (next != null) {
                current = next;
            } else {
                return 0;
            }
        }

        return current.weight;
    }

    /**
     * Codes a trie into bites.
     * @param out -- OutputStream object what takes coding result
     * @throws IOException
     * @throws IllegalArgumentException if OutputStream object is a null
     */
    @Override
    public void serialize(@NotNull OutputStream out) throws IOException {

    }

    /**
     * Decodes a trie from bites.
     * @param in -- InputStream object what gets a code
     * @throws IOException
     * @throws IllegalArgumentException if InputStream object is a null
     */
    @Override
    public void deserialize(@NotNull InputStream in) throws IOException {

    }

    /** Creates a new branch in a trie */
    private Node createNewBranch(@Nullable String element) {
        Node first = new Node();
        first.next = new HashMap<>();
        first.weight = 1;
        Node prev = first;

        if (element != null) {
            for (char symb : element.toCharArray()) {
                Node node = new Node();
                node.next = new HashMap<>();
                node.weight = 1;

                prev.next.put(symb, node);
                prev = node;
            }
        }

        prev.isTerminal = true;
        return first;
    }

    /**
     * Codes a subtree. Used only as a part of serialize.
     * @param node -- a root of subtree
     * @param nodeCode -- a minimum positive value of a non used node code
     * @return a minimum positive value of a non used node code after coding
     */
    private int codeSubtree(@NotNull OutputStream out, Node node, int nodeCode) throws IOException {

    }
}