package com.example.Trie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A Trie (http://neerc.ifmo.ru/wiki/index.php?title=%D0%91%D0%BE%D1%80).
 * Implements interface Serializable.
 * */
public class Trie implements Serializable {

    /** A node of a trie. */
    private class Node {

        /** A HashMap contains references to the next nodes. */
        private HashMap<Character, Node> next;

        /** A number of elements passing through this node. */
        int weight;

        private boolean isTerminal;
    }

    /** A root of a trie. */
    @Nullable private Node root;

    /** A number of containing strings. */
    private int size;

    /** A number of containing nodes. */
    private int treeSize;

    Trie() {
        root = null;
        size = 0;
        treeSize = 0;
    }

    /**
     * Adds a string into a trie.
     * @param element -- a string added in trie
     * @throws IllegalArgumentException if an element is null
     * @return true if an element wasn't in a trie before adding
     */
    public boolean add(@NotNull String element) {
        if (contains(element)) {
            return false;
        }

        size++;
        if (root == null) {
            root = createNewBranch(element);
            return true;
        }

        Node current = root;
        Node next;
        boolean isSubstring = true;

        for (int i = 0; i < element.length(); i++) {
            next = current.next.get(element.charAt(i));
            current.weight++;

            if (next != null) {
                current = next;
            } else {
                next = createNewBranch(element.substring(i + 1));
                current.next.put(element.charAt(i), next);
                isSubstring = false;
                break;
            }
        }

        if (isSubstring) {
            current.isTerminal = true;
        }

        return true;
    }

    /**
     * Checks an availability of an element.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element is in a trie
     */
    public boolean contains(@NotNull String element) {
        if (root == null) {
            return false;
        }

        Node current = root;
        Node next;

        for (char symb: element.toCharArray()) {
            next = current.next.get(symb);

            if (next != null) {
                current = next;
            } else {
                return false;
            }
        }

        return current.isTerminal;
    }

    /**
     * Removes an element from a trie.
     * @param element -- an element going been removed
     * @throws IllegalArgumentException if an element is null
     * @return true if an element was in a trie before removing
     */
    public boolean remove(@NotNull String element) {
        if (!contains(element)) {
            return false;
        }

        root.weight--;
        size--;
        if (root.weight == 0) {
            root = null;
            treeSize = 0;
            return true;
        }

        Node current = root;
        Node next;

        for (int i = 0; i < element.length(); i++) {
            next = current.next.get(element.charAt(i));

            if (next.weight > 1) {
                next.weight--;
                current = next;
            } else {
                current.next.remove(element.charAt(i));
                treeSize -= (element.length() - i + 1);
                return true;
            }
        }
        current.isTerminal = false;

        return true;
    }

    /**
     * Returns a size of a trie.
     * @return number of containing strings
     */
    public int size() {
        return size;
    }

    /**
     * Returns the number of strings what have a matching prefix.
     * @throws IllegalArgumentException if a prefix is null
     * @return the number of strings starts with a prefix
     */
    public int howManyStartsWithPrefix(@NotNull String prefix) {
        Node current = root;
        Node next;

        for (char symb: prefix.toCharArray()) {
            next = current.next.get(symb);

            if (next != null) {
                current = next;
            } else {
                return 0;
            }
        }

        return current.weight;
    }

    /**
     * Codes a trie into bites.
     * @param out -- an OutputStream object what takes coding result
     * @throws IllegalArgumentException if an OutputStream object is a null
     */
    @Override
    public void serialize(@NotNull OutputStream out) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(out);

        if (root == null) {
            objectOut.writeInt(0);
        } else {
            objectOut.writeInt(treeSize);
            objectOut.writeInt(size);
            objectOut.writeInt(root.hashCode());
            codeSubtree(objectOut, root);
        }

        objectOut.flush();
    }

    /**
     * Decodes a trie from bites.
     * @param in -- an InputStream object what gets a code
     * @throws IllegalArgumentException if an InputStream object is a null
     */
    @Override
    public void deserialize(@NotNull InputStream in) throws IOException {
        ObjectInputStream objectIn = new ObjectInputStream(in);

        HashMap<Integer, Node> nodes = new HashMap<>();
        treeSize = objectIn.readInt();
        if (treeSize == 0) {
            root = null;
            size = 0;
            return;
        }
        size = objectIn.readInt();
        int rootHashCode = objectIn.readInt();

        for (int i = 0; i < treeSize; i++) {
            int hashCode = objectIn.readInt();
            Node node = nodes.get(hashCode);
            if (node == null) {
                node = new Node();
                node.next = new HashMap<>();
                nodes.put(hashCode, node);
            }

            node.weight = objectIn.readInt();
            node.isTerminal = objectIn.readBoolean();

            int mapSize = objectIn.readInt();
            for (int j = 0; j < mapSize; j++) {
                char symb = objectIn.readChar();
                hashCode = objectIn.readInt();

                Node next = nodes.get(hashCode);
                if (next == null) {
                    next = new Node();
                    next.next = new HashMap<>();
                    nodes.put(hashCode, next);
                }

                node.next.put(symb, next);
            }
        }

        root = nodes.get(rootHashCode);
    }

    /** Creates a new branch in a trie */
    private Node createNewBranch(@Nullable String element) {
        Node first = new Node();
        first.next = new HashMap<>();
        first.weight = 1;
        Node prev = first;
        treeSize++;

        if (element != null) {
            for (char symb : element.toCharArray()) {
                Node node = new Node();
                node.next = new HashMap<>();
                node.weight = 1;

                prev.next.put(symb, node);
                prev = node;
                treeSize++;
            }
        }

        prev.isTerminal = true;
        return first;
    }

    /**
     * Codes a subtree. Used only as a part of serialize.
     * @param node -- a root of subtree
     */
    private void codeSubtree(@NotNull ObjectOutputStream out, @NotNull Node node) throws IOException {
        out.writeInt(node.hashCode());
        out.writeInt(node.weight);
        out.writeBoolean(node.isTerminal);
        out.writeInt(node.next.size());

        for (Map.Entry entry: node.next.entrySet()) {
            out.writeChar((Character) entry.getKey());
            out.writeInt(entry.getValue().hashCode());
        }

        for (Map.Entry entry: node.next.entrySet()) {
            codeSubtree(out, (Node) entry.getValue());
        }
    }
}