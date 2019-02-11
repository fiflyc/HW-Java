package com.example.HashTable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Math.abs;

/** Hash table containing String objects. */
public class HashTable {

    /** The number of keys in the hash table. */
    private int size;

    /** Container with pairs (key, value). */
    private ArrayList<List> container;

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

        container = new ArrayList<>(maxHash);
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
     * @throws IllegalArgumentException if the key has null value
     * @return true if the key is in the hash table
     */
    public boolean contains(@NotNull String key) {
        return get(key) != null;
    }

    /**
     * Returns a string by the key.
     * @throws IllegalArgumentException if the key has null value
     * @return the string corresponding to the key
     */
    public String get(@NotNull String key) {
        return container.get(index(key)).find(key);
    }

    /**
     * Puts the string in the hash table.
     * @throws IllegalArgumentException if the key or the value has null value
     * @return the string corresponding to the key before putting the new one, null if there is no old ones
     */
    public String put(@NotNull String key, @NotNull String value) {
        String result = remove(key);
        container.get(index(key)).add(key, value);
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
     * @throws IllegalArgumentException if the key has null value
     * @return the removed string or null if there is nothing removed
     */
    public String remove(@NotNull String key) {
        if (contains(key)) {
            size--;
        }

        return container.get(index(key)).remove(key);
    }

    /** Clears the hash table. */
    public void clear() {
        for (int i = 0; i < container.size(); i++) {
            container.get(i).clear();
        }

        container = new ArrayList<>(DEFAULT_MAX_HASH);
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

        var newContainer = new ArrayList<List>();
        for (int i = 0; i < size; i++) {
            newContainer.add(new List());
        }

        Iterator it = container.iterator();
        Object[] keys;
        while (it.hasNext()) {
            keys = ((List) it.next()).getAllKeys().toArray();

            for (int i = 0; i < keys.length; i++) {
                var key = (String) keys[i];
                newContainer.get(index(key, size)).add(key, get(key));
            }
        }

        container = newContainer;
    }

    /** Finds a correct List in the container for a key. */
    private int index(String key) {
        return abs(key.hashCode()) % container.size();
    }

    /** Evaluates a correct hash code for a key using size of a container. */
    private int index(String key, int size) {
        return abs(key.hashCode()) % size;
    }
}
