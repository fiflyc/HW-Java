package com.example.HashTable;

import static java.lang.Math.abs;

/** Hash table containing String objects. */
public class HashTable {

    /** The number of keys in the hash table. */
    private int size;
    /** Container with pairs (key, value). */
    private List[] container;
    /** The default max hash value. */
    private static final int DEFAULT_MAX_HASH = 10000;

    HashTable() {
        this(DEFAULT_MAX_HASH);
    }

    HashTable(int maxHash) {
        if (maxHash <= 0) {
            maxHash = DEFAULT_MAX_HASH;
        }

        container = new List[maxHash];
        for (int i = 0; i < maxHash; i++) {
            container[i] = new List();
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
        return container[abs(key.hashCode()) % container.length].find(key);
    }

    /**
     * Puts the string in the hash table.
     * @return the string corresponding to the key before putting the new one, null if there is no old ones
     */
    public String put(String key, String value) {
        String result = remove(key);
        container[abs(key.hashCode()) % container.length].add(key, value);
        if (result == null)
            size++;

        return result;
    }

    /**
     * Removes a string from the hash table.
     * @return the removed string or null if there is nothing removed
     */
    public String remove(String key) {
        if (contains(key))
            size--;

        return container[abs(key.hashCode()) % container.length].remove(key);
    }

    /** Clears the hash table. */
    public void clear() {
        for (int i = 0; i < container.length; i++) {
            container[i].clear();
        }
        size = 0;
    }
}
