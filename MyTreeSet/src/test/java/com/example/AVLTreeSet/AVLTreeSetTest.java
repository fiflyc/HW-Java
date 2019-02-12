package com.example.AVLTreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeSetTest {

    private AVLTreeSet<Integer> treeSet;

    @BeforeEach
    void createTreeSet() {
        treeSet = new AVLTreeSet<>();
    }

    @Test
    void iterator_IterateTillEnd_FindsAllElements() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        Iterator iterator = treeSet.iterator();
        for (int i = 0; i < 10; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void iterator_IteratingOnDescendingSet_IteratesCorrectly() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        Iterator iterator = treeSet.descendingSet().iterator();
        for (int i = 9; i >= 0; i--) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorHasNext_EndOfIteration_NoSuchElementExpression() {
        treeSet.add(1);
        Iterator iterator = treeSet.iterator();
        iterator.next();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iterator_AddElementInTreeSet_IteratorDisabled() {
        treeSet.add(0);
        Iterator iterator = treeSet.iterator();
        treeSet.add(1);

        assertThrows(IllegalStateException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::hasNext);
    }

    @Test
    void iterator_RemoveElementFromTreeSet_IteratorDisabled() {
        treeSet.add(0);
        Iterator iterator = treeSet.iterator();
        treeSet.remove(0);

        assertThrows(IllegalStateException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::hasNext);
    }

    @Test
    void descendingIterator_IterateTillEnd_FindsAllElements() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        Iterator iterator = treeSet.descendingIterator();
        for (int i = 9; i >= 0; i--) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void add_AddExistElement_ReturnFalse() {
        treeSet.add(1);

        assertFalse(treeSet.add(1));
    }

    @Test
    void remove_RemoveNotExistElement_ReturnFalse() {
        assertFalse(treeSet.remove(1));
    }

    @Test
    void contains_AfterAddingElements_FinsAll() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        for (int i = 0; i < 10; i++) {
            assertTrue(treeSet.contains(i));
        }
    }

    @Test
    void contains_AfterRemovingElements_FindsOnlyContains() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        for (int i = 0; i < 10; i += 2) {
            treeSet.remove(i);
        }

        for (int i = 0; i < 10; i += 2) {
            assertFalse(treeSet.contains(i));
        }

        for (int i = 1; i < 10; i += 2) {
            assertTrue(treeSet.contains(i));
        }
    }

    @Test
    void size_AfterAddingElements_CorrectValue() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        assertEquals(10, treeSet.size());
    }

    @Test
    void size_AfterRemovingElements_CorrectValue() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        for (int i = 0; i < 10; i += 2) {
            treeSet.remove(i);
        }

        assertEquals(5, treeSet.size());
    }

    @Test
    void first_AfterAddingElements_CorrectElement() {
        for (int i = 9; i >= 0; i--) {
            treeSet.add(i);
            assertEquals(Integer.valueOf(i), treeSet.first());
        }
    }

    @Test
    void first_AfterRemovingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        for (int i = 0; i < 9; i++) {
            treeSet.remove(i);
            assertEquals(Integer.valueOf(i + 1), treeSet.first());
        }
    }

    @Test
    void last_AfterAddingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
            assertEquals(Integer.valueOf(i), treeSet.last());
        }
    }

    @Test
    void last_AfterRemovingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }

        for (int i = 9; i > 0; i--) {
            treeSet.remove(i);
            assertEquals(Integer.valueOf(i - 1), treeSet.last());
        }
    }

    @Test
    void lower_AfterAddingElements_CorrectElement() {
        for (int i = 9; i >= 0; i--) {
            treeSet.add(i);
            assertNull(treeSet.lower(i));
            assertEquals(Integer.valueOf(i), treeSet.lower(i + 1));
        }
    }

    @Test
    void lower_AfterRemovingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        for (int i = 0; i < 10; i++) {
            treeSet.remove(i);
            assertNull(treeSet.lower(i + 1));
        }
    }

    @Test
    void higher_AfterAddingElements_CorrectElement() {
        for (int i = 0; i < 9; i++) {
            treeSet.add(i);
            assertNull(treeSet.higher(i));
            assertEquals(Integer.valueOf(i), treeSet.higher(i - 1));
        }
    }

    @Test
    void higher_AfterRemovingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        for (int i = 9; i > 0; i--) {
            treeSet.remove(i);
            assertNull(treeSet.higher(i - 1));
        }
    }

    @Test
    void floor_AfterAddingElements_CorrectElement() {
        for (int i = 9; i >= 0; i--) {
            treeSet.add(i);
            assertEquals(Integer.valueOf(i), treeSet.floor(i));
        }
    }

    @Test
    void floor_AfterRemovingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        for (int i = 0; i < 10; i++) {
            treeSet.remove(i);
            assertNull(treeSet.floor(i));
        }
    }

    @Test
    void ceiling_AfterAddingElements_CorrectElement() {
        for (int i = 0; i < 9; i++) {
            treeSet.add(i);
            assertNull(treeSet.ceiling(i + 1));
            assertEquals(Integer.valueOf(i), treeSet.ceiling(i));
        }
    }

    @Test
    void ceiling_AfterRemovingElements_CorrectElement() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        for (int i = 9; i > 0; i--) {
            treeSet.remove(i);
            assertNull(treeSet.ceiling(i));
        }
    }
}