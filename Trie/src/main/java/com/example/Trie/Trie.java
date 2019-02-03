package com.example.Trie;

import org.jetbrains.annotations.NotNull;

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

        /** A HashMap contains references to the next nodes */
        private HashMap<Character, Node> next;

        private boolean isTerminal;
    }

    /** A root of a trie */
    private Node root;

    /** A size of a trie */
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

    }

    /**
     * Checks an availability of an element.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element is in a trie
     */
    public boolean contains(@NotNull String element) {

    }

    /**
     * Removes an element from a trie.
     * @param element
     * @throws IllegalArgumentException if an element is null
     * @return true if an element was in a trie before removing
     */
    public boolean remove(@NotNull String element) {

    }

    /**
     * Returns a size of a trie.
     * @return number of containing strings
     */
    public int size() {

    }

    /**
     * Returns the number of strings have a matching prefix.
     * @param prefix
     * @throws IllegalArgumentException if a prefix is null
     * @return the number of strings starts with a prefix
     */
    public int howManyStartsWithPrefix(@NotNull String prefix) {

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
     * Decodes a trie from bites
     * @param in -- InputStream object what gets a code
     * @throws IOException
     * @throws IllegalArgumentException if InputStream object is a null
     */
    @Override
    public void deserialize(@NotNull InputStream in) throws IOException {

    }

    /** Executes when a root of a trie is null. */
    private Node addFirstElement(@NotNull String element) {

    }
}