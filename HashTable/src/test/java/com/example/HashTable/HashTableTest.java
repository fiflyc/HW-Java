package com.example.HashTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void size() {
        var hashTable = new HashTable();

        hashTable.put("2017", "chicken");
        hashTable.put("2018", "dog");
        hashTable.put("2019", "pig");
        hashTable.put("2020", "rat");

        assertEquals(hashTable.size(), 4);

        hashTable.remove("2017");
        hashTable.remove("2019");

        assertEquals(hashTable.size(), 2);

        hashTable.put("2022", "tiger");
        hashTable.put("2024", "dragon");

        assertEquals(hashTable.size(), 4);

        hashTable.clear();

        assertEquals(hashTable.size(), 0);
    }

    @Test
    void contains() {
        var hashTable = new HashTable();

        hashTable.put("19", "natural");
        hashTable.put("sqrt(2)", "irrational");
        hashTable.put("1+i", "complex");

        assertFalse(hashTable.contains("9/10"));
        assertTrue(hashTable.contains("1+i"));
        assertTrue(hashTable.contains("19"));

        hashTable.remove("19");

        assertFalse(hashTable.contains("19"));
    }

    @Test
    void get() {
        var hashTable = new HashTable();

        hashTable.put("19", "natural");
        hashTable.put("sqrt(2)", "irrational");
        hashTable.put("1+i", "complex");

        assertNull(hashTable.get("9/10"));
        assertEquals(hashTable.get("1+i"), "complex");
        assertEquals(hashTable.get("19"), "natural");

        hashTable.remove("19");

        assertNull(hashTable.get("19"));
    }

    @Test
    void put() {
        var hashTable = new HashTable();

        hashTable.put("0", "I solemnly swear");
        hashTable.put("1", "that");
        hashTable.put("2", "I am up to no good");

        assertEquals(hashTable.get("0"), "I solemnly swear");
        assertEquals(hashTable.get("1"), "that");
        assertEquals(hashTable.get("2"), "I am up to no good");
        assertNull(hashTable.get("The Marauder’s Map"));
    }

    @Test
    void remove() {
        var hashTable = new HashTable();

        hashTable.put("2017", "chicken");
        hashTable.put("2018", "dog");
        hashTable.put("2019", "pig");
        hashTable.put("2020", "rat");

        hashTable.remove("2017");
        hashTable.remove("2019");

        assertNull(hashTable.get("2017"));
        assertEquals(hashTable.get("2018"), "dog");

        hashTable.put("2022", "tiger");
        hashTable.put("2024", "dragon");

        assertNull(hashTable.remove("2019"));
        assertEquals(hashTable.remove("2020"), "rat");
    }

    @Test
    void clear() {
        var hashTable = new HashTable();

        hashTable.put("1", "Out in the distance");
        hashTable.put("2", "There's so much gold");
        hashTable.put("3,", "The treasure that I've found");
        hashTable.put("4", "Is more than enough");
        hashTable.put("5", "Far to the hill we've to go");
        hashTable.clear();

        for (int i = 0; i < 6; i++) {
            assertNull(hashTable.get(Integer.toString(i)));
        }
    }
}