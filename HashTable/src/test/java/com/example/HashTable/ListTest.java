package com.example.HashTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void add() {
        var list = new List();

        list.add("0", "I solemnly swear");
        list.add("1", "that");
        list.add("2", "I am up to no good");

        assertEquals(list.find("0"), "I solemnly swear");
        assertEquals(list.find("1"), "that");
        assertEquals(list.find("2"), "I am up to no good");
        assertNull(list.find("The Marauder’s Map"));
    }

    @Test
    void remove() {
        var list = new List();

        list.add("2017", "chicken");
        list.add("2018", "dog");
        list.add("2019", "pig");
        list.add("2020", "rat");

        list.remove("2017");
        list.remove("2019");

        assertNull(list.find("2019"));
        assertEquals(list.find("2018"), "dog");

        list.add("2022", "tiger");
        list.add("2024", "dragon");

        assertNull(list.remove("2019"));
        assertEquals(list.remove("2020"), "rat");
    }

    @Test
    void find() {
        var list = new List();

        list.add("19", "natural");
        list.add("sqrt(2)", "irrational");
        list.add("1+i", "complex");

        assertNull(list.find("9/10"));
        assertEquals(list.find("1+i"), "complex");
        assertEquals(list.find("19"), "natural");

        list.remove("19");

        assertNull(list.find("19"));
    }

    @Test
    void clear() {
        var list = new List();

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