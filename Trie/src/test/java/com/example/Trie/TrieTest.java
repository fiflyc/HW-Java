package com.example.Trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void createTrie() {
        trie = new Trie();
    }

    @Test
    void howManyStartsWithPrefix_StringsWithCommonPrefix_CorrectValue() {
        String string = "Blind Guardian -- War of the Thrones";
        String substring1 = "Blind Guardian";
        String substring2 = "Blind";

        trie.add(string);
        trie.add(substring1);
        trie.add(substring2);

        assertEquals(0, trie.howManyStartsWithPrefix("War of the Thrones"));
        assertEquals(1, trie.howManyStartsWithPrefix("Blind Guardian "));
        assertEquals(2, trie.howManyStartsWithPrefix("Blind "));
        assertEquals(3, trie.howManyStartsWithPrefix("B"));
    }

    @Test
    void contains_AddStringAndSubstring_FindsBoth() {
        String string = "Nothing will grow here";
        String substring = "Nothing";

        trie.add(string);
        trie.add(substring);

        assertTrue(trie.contains(string));
        assertTrue(trie.contains(substring));
    }

    @Test
    void contains_AddStringWithPrefixRemoveString_FindsPrefix() {
        String string = "Icy fields - blackened sorrow";
        String prefix = "Icy";

        trie.add(prefix);
        trie.add(string);
        trie.remove(string);

        assertTrue(trie.contains(prefix));
        assertFalse(trie.contains(string));
    }

    @Test
    void contains_AddStringWithPrefixRemovePrefix_FindsString() {
        String string = "Legacy of a lost mind";
        String prefix = "Legacy";

        trie.add(prefix);
        trie.add(string);
        trie.remove(prefix);

        assertFalse(trie.contains(prefix));
        assertTrue(trie.contains(string));
    }

    @Test
    void contains_AddStringsWithDifferentFirstSymbolsRemoveOne_FindsOther() {
        String string1 = "Feed my void";
        String string2 = "What you're waiting for?";

        trie.add(string1);
        trie.add(string2);
        trie.remove(string1);

        assertFalse(trie.contains(string1));
        assertTrue(trie.contains(string2));
    }

    @Test
    void contains_AddStringsWithCommonPrefixRemoveOne_FindsOther() {
        String string1 = "I'm too late";
        String string2 = "It is more than a game";

        trie.add(string1);
        trie.add(string2);
        trie.remove(string1);

        assertFalse(trie.contains(string1));
        assertTrue(trie.contains(string2));
    }

    @Test
    void size_AddDifferentStrings_CorrectValue() {
        String string1 = "The river reveals";
        String string2 = "Now I'm in between these lines";

        trie.add(string1);
        trie.add(string2);

        assertEquals(2, trie.size());
    }

    @Test
    void size_RemoveString_CorrectValue() {
        String string = "I cannot escape it seems";

        trie.add(string);
        trie.remove(string);

        assertEquals(0, trie.size());
    }

    @Test
    void size_EmptyTrie_ZeroValue() {
        assertEquals(0, trie.size());
    }

    @Test
    void add_AddOneStringSeveralTime_AddOnce() {
        String string = "Sail on, my friend";

        assertTrue(trie.add(string));
        assertFalse(trie.add(string));

        trie.remove(string);

        assertFalse(trie.contains(string));
    }

    @Test
    void deserialize_DecodingAfterSerialize_CorrectTrie() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String string1 = "All I ever feel is";
        String string2 = "All I ever see is";
        String string3 = "Walls they fall";
        String string4 = "When the march of the others begins";

        trie.add(string1);
        trie.add(string2);
        trie.add(string3);
        trie.add(string4);
        trie.serialize(out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        trie.deserialize(in);

        assertTrue(trie.contains(string1));
        assertTrue(trie.contains(string2));
        assertTrue(trie.contains(string3));
        assertTrue(trie.contains(string4));
        assertEquals(4, trie.size());

        out.close();
        in.close();
    }

    @Test
    void deserialize_DecodingEmptyTrie_ZeroSizedTrie() throws IOException {
        try (var out = new ByteArrayOutputStream()) {
            trie.serialize(out);

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

            trie.deserialize(in);

            assertEquals(0, trie.size());
        }
    }
}