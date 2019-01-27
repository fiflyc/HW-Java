package com.example.HashTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    private List list;

    @BeforeEach
    void initialize() {
        list = new List();
    }

    @Test
    void Find_AfterAddingElements_FindsAll() {
        list.add("0", "I solemnly swear");
        list.add("1", "that");
        list.add("2", "I am up to no good");
        list.add("3", "(The Marauder’s Map)");

        assertEquals("I solemnly swear", list.find("0"));
        assertEquals("that", list.find("1"));
        assertEquals("I am up to no good", list.find("2"));
        assertEquals("(The Marauder’s Map)", list.find("3"));
    }

    @Test
    void Find_AfterRemovingElements_NullIfRemoved() {
        list.add("2017", "chicken");
        list.add("2018", "dog");
        list.add("2019", "pig");
        list.add("2020", "rat");

        list.remove("2017");
        list.remove("2019");

        assertNull(list.find("2017"));
        assertNull(list.find("2019"));
    }

    @Test
    void Find_AfterRemovingElements_FindsNotRemoved() {
        list.add("2017", "chicken");
        list.add("2018", "dog");
        list.add("2019", "pig");
        list.add("2020", "rat");

        list.remove("2017");
        list.remove("2019");

        assertEquals("rat", list.find("2020"));
        assertEquals("dog", list.find("2018"));
    }

    @Test
    void Find_AfterClearingTable_Null() {
        list.add("1", "Out in the distance");
        list.add("2", "There's so much gold");
        list.add("3,", "The treasure that I've found");
        list.add("4", "Is more than enough");
        list.add("5", "Far to the hill we've to go");
        list.clear();

        for (int i = 0; i < 6; i++) {
            assertNull(list.find(Integer.toString(i)));
        }
    }
}