package com.example.HashTable;

import java.util.Vector;

import static java.lang.Math.abs;

/** Hash table containing String objects. */
public class HashTable {

    /** The number of keys in the hash table. */
    private int size;

    /** Container with pairs (key, value). */
    private Vector<List> container;

    /** The default max hash value. */
    private static final int DEFAULT_MAX_HASH = 10000;

    /** The valid maximum of a value size / container.size() */
    private static final float MAX_VALID_FILLING = (float) 0.75;

    HashTable() {
        this(DEFAULT_MAX_HASH);
    }

    HashTable(int maxHash) {
        if (maxHash <= 0) {
            maxHash = DEFAULT_MAX_HASH;
        }

        container = new Vector<List>(maxHash);
        for (int i = 0; i < maxHash; i++) {
            container.add(new List());
        }

        size = 0;
    }

    /**
     * Returns the size of the hash table.
     * @return the number of containing keys
     */
    public int size() {
        return size;
    }

    /**
     * Checks an availability of a string by the key.
     * @return true if the key is in the hash table
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Returns a string by the key.
     * @return the string corresponding to the key
     */
    public String get(String key) {
        return container.get(abs(key.hashCode()) % container.size()).find(key);
    }

    /**
     * Puts the string in the hash table.
     * @return the string corresponding to the key before putting the new one, null if there is no old ones
     */
    public String put(String key, String value) {
        String result = remove(key);
        container.get(abs(key.hashCode()) % container.size()).add(key, value);
        if (result == null) {
            size++;
        }

        if (size >= MAX_VALID_FILLING * container.size()) {
            increaseSize(size * 2);
        }

        return result;
    }

    /**
     * Removes a string from the hash table.
     * @return the removed string or null if there is nothing removed
     */
    public String remove(String key) {
        if (contains(key)) {
            size--;
        }

        return container.get(abs(key.hashCode()) % container.size()).remove(key);
    }

    /** Clears the hash table. */
    public void clear() {
        for (int i = 0; i < container.size(); i++) {
            container.get(i).clear();
        }

        container = new Vector<List>(DEFAULT_MAX_HASH);
        for (int i = 0; i < DEFAULT_MAX_HASH; i++) {
            container.add(new List());
        }

        size = 0;
    }

    /** Increase a size of the container. */
    private void increaseSize(int size) {
        if (size <= container.size()) {
            return;
        }

        while (container.size() < size) {
            container.add(new List());
        }
    }
}
