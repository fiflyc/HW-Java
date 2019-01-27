package com.example.HashTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    HashTable hashTable;

    @BeforeEach
    void initialize() {
        hashTable = new HashTable();
    }

    @Test
    void Put_AddingNullValue_Exception() {
        try {
            hashTable.put("first try", null);
        } catch (IllegalArgumentException e) {
            return;
        }

        assert false;
    }

    @Test
    void Put_AddingNullKey_Exception() {
        try {
            hashTable.put(null, "not null");
        } catch (IllegalArgumentException e) {
            return;
        }

        assert false;
    }

    @Test
    void Size_AfterAddingElements_CorrectValue() {
        hashTable.put("2017", "chicken");
        hashTable.put("2018", "dog");
        hashTable.put("2019", "pig");
        hashTable.put("2020", "rat");

        assertEquals(4, hashTable.size());
    }

    @Test
    void Size_AfterRemovingElements_CorrectValue() {
        hashTable.put("2017", "chicken");
        hashTable.put("2018", "dog");
        hashTable.put("2019", "pig");
        hashTable.put("2020", "rat");

        hashTable.remove("2017");
        hashTable.remove("2019");

        assertEquals(2, hashTable.size());
    }

    @Test
    void Size_AfterClearingTable_ZeroValue() {
        hashTable.put("2017", "chicken");
        hashTable.put("2018", "dog");
        hashTable.put("2019", "pig");
        hashTable.put("2020", "rat");

        hashTable.clear();

        assertEquals(0, hashTable.size());
    }

    @Test
    void Contains_AfterAddingElements_FindsAll() {
        hashTable.put("19", "natural");
        hashTable.put("sqrt(2)", "irrational");
        hashTable.put("1+i", "complex");

        assertTrue(hashTable.contains("sqrt(2)"));
        assertTrue(hashTable.contains("1+i"));
        assertTrue(hashTable.contains("19"));
    }

    @Test
    void Contains_AfterRemovingElements_FalseIfRemoved() {
        hashTable.put("19", "natural");
        hashTable.put("sqrt(2)", "irrational");
        hashTable.put("1+i", "complex");

        hashTable.remove("19");

        assertFalse(hashTable.contains("19"));

        hashTable.remove("1+i");

        assertFalse(hashTable.contains("1+i"));

        hashTable.remove("sqrt(2)");

        assertFalse(hashTable.contains("sqrt(2)"));
    }

    @Test
    void Get_AfterAddingElements_FindsAll() {
        hashTable.put("19", "natural");
        hashTable.put("sqrt(2)", "irrational");
        hashTable.put("1+i", "complex");

        assertEquals("irrational", hashTable.get("sqrt(2)"));
        assertEquals("complex", hashTable.get("1+i"));
        assertEquals("natural", hashTable.get("19"));
    }

    @Test
    void Get_AfterRemovingElements_NullIfRemoved() {
        hashTable.put("19", "natural");
        hashTable.put("sqrt(2)", "irrational");
        hashTable.put("1+i", "complex");

        hashTable.remove("19");

        assertNull(hashTable.get("19"));

        hashTable.remove("1+i");

        assertNull(hashTable.get("1+i"));

        hashTable.remove("sqrt(2)");

        assertNull(hashTable.get("sqrt(2)"));
    }

    @Test
    void Get_AfterClearingTable_ZeroValue() {
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