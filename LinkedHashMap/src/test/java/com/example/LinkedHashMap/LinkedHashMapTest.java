package com.example.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapTest {
    LinkedHashMap<Integer, Integer> hashMap;

    @BeforeEach
    void initialize() {
        hashMap = new LinkedHashMap<>();
    }

    @Test
    void entrySetIterator_AfterAddingElements_FindsAll() {
        hashMap.put(0, 0);
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);

        var iterator = hashMap.entrySet().iterator();
        for (int i = 0; i < 4; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().getValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void entrySetIterator_AfterAddingAndRemovingElements_FindsRemains() {
        hashMap.put(0, 0);
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);
        hashMap.remove(2);
        hashMap.remove(3);

        var iterator = hashMap.entrySet().iterator();
        for (int i = 0; i < 2; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().getValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void size_AfterAddingElements_CorrectValue() {
        hashMap.put(0, 0);
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);

        assertEquals(4, hashMap.size());
    }

    @Test
    void size_AfterAddingAndRemovingElements_CorrectValue() {
        hashMap.put(0, 0);
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);
        hashMap.remove(2);
        hashMap.remove(3);

        assertEquals(2, hashMap.size());
    }

    @Test
    void entrySetSize_AfterAddingElements_CorrectValue() {
        hashMap.put(0, 0);
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);

        assertEquals(4, hashMap.entrySet().size());
    }

    @Test
    void entrySetSize_AfterAddingAndRemovingElements_CorrectValue() {
        hashMap.put(0, 0);
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);
        hashMap.remove(2);
        hashMap.remove(3);

        assertEquals(2, hashMap.entrySet().size());
    }
}