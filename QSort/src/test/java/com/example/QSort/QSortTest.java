package com.example.QSort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QSortTest {

    private ArrayList<Integer> array;

    @BeforeEach
    void initialize() {
        array = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            array.add(99 - i);
        }
    }

    @Test
    void sort_badBounds_NothingChanged() {
        QSort.sort(array, 10, 0);
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(99 - i), array.get(i));
        }
    }

    @Test
    void sort_equalBounds_NothingChanged() {
        QSort.sort(array, 5, 5);
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(99 - i), array.get(i));
        }
    }

    @Test
    void sort_someArray_SortedArray() {
        QSort.sort(array, 0, 99);
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(i), array.get(i));
        }
    }
}