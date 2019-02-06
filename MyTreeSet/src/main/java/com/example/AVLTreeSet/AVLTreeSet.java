package com.example.AVLTreeSet;

import com.example.MyTreeSet.MyTreeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

/**
 * TreeSet, containing AVL tree.
 * Implements interface MyTreeSet.
 * @param <T> -- type of elements in TreeSet
 */
public class AVLTreeSet<T> extends AbstractSet<T> implements MyTreeSet<T> {

    /** Node of a tree. */
    private class Node<T> {

        /** A value in a node. */
        @NotNull private T value;

        /** Height of a subtree. */
        private int height;

        /** Size of a subtree. */
        private int size;

        /** Left child. */
        @Nullable private Node left;

        /** Right child. */
        @Nullable private Node right;

        Node(T value) {
            this.value = value;
            left = null;
            right = null;
            height = 1;
            size = 1;
        }
    }

    /** Root of a tree */
    @Nullable private Node root;

    /**
     * Compares elements.
     * If type T or his parent does not implement Comparable<>, AVLTreeSet will no work.
     */
    @Nullable private Comparator<? super T> comparator;

    AVLTreeSet() {
        root = null;
        comparator = null;
    }

    AVLTreeSet(@NotNull Comparator<? super T> comparator) {
        root = null;
        this.comparator = comparator;
    }

    /**
     * Adds element into container.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element wasn't in container before adding
     */
    public boolean add(@NotNull T element) {
        if (contains(element)) {
            return false;
        }

        add(root, element);

        return false;
    }

    /**
     * Removes element from container.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element was in a container before removing
     */
    public boolean remove(@NotNull Object element) {
        if (!contains(element)) {
            return false;
        }

        remove(root, (T) element);

        return false;
    }

    /**
     * Checks an element availability.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element in a container
     */
    public boolean contains(@NotNull Object element) {
        Node current = root;

        while (current != null) {
            if (compare(element, (T) current.value) < 0) {
                current = current.left;
            } else if (compare(element, (T) current.value) > 0) {
                current = current.right;
            } else {
                return true;
            }
        }

        return false;
    }

    /** Returns number of elements in container. */
    @Override
    public int size() {
        if (root == null) {
            return 0;
        }

        return root.size;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }

    @Override
    public MyTreeSet<T> descendingSet() {
        return null;
    }

    @Override
    public T first() {
        return null;
    }

    @Override
    public T last() {
        return null;
    }

    @Override
    public T lower(@NotNull T t) {
        return null;
    }

    @Override
    public T floor(@NotNull T t) {
        return null;
    }

    @Override
    public T ceiling(@NotNull T t) {
        return null;
    }

    @Override
    public T higher(@NotNull T t) {
        return null;
    }

    private int compare(@NotNull Object x, @NotNull T y) {
        if (comparator != null) {
            return comparator.compare((T) x, y);
        }

        return ((Comparable<? super T>) x).compareTo(y);
    }

    /**
     * Adds a value into a subtree.
     * @param node -- the root of a subtree
     * @return the of a new subtree
     */
    private Node add(@NotNull Node node, T value) {
        if (node == null) {
            node = new Node(value);
        } else if (compare(node.value, value) > 0) {
            node.left = add(node.left, value);
        } else {
            node.right = add(root.right, value);
        }

        update(node);

        return balance(node);
    }

    /**
     * Removes a value from a subtree.
     * @param node -- the root of a subtree
     * @return the root of a new subtree
     */
    private Node remove(@NotNull Node node, T value) {
        if (compare(node.value, value) < 0) {
            node.right = remove(node.right, value);
        } else if (compare(node.value, value) > 0) {
            node.left = remove(node.left, value);
        } else {
            if (node.right == null) {
                node = node.left;
            } else if (node.left == null) {
                node = node.right;
            } else {
                Node v = node.right;
                while (v.left != null) {
                    v = v.left;
                }

                T temp = (T) v.value;
                v.value = node.value;
                node.value = temp;

                node.right = remove(node.right, value);
            }
        }

        update(node);

        return balance(node);
    }

    /** Balances a tree. */
    private Node balance(@Nullable Node node) {
        if (node == null) {
            return null;
        }

        if (height(node.left) == height(node.right) + 2) {
            if (height(node.left.left) >= height(node.left.right)) {
                node = rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        } else if (height(node.right) == height(node.left) + 2) {
            if (height(node.right.right) >= height(node.right.left)) {
                node = rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }

        return node;
    }

    /** Double left rotation. */
    private Node rotateLeft(@NotNull Node node) {
        Node v = node.right;

        node.right = v.left;
        v.left = node;
        update(node);
        update(v);

        return v;
    }

    /** Double right rotation. */
    private Node rotateRight(@NotNull Node node) {
        Node v = node.left;

        node.left = v.right;
        v.right = node;
        update(node);
        update(v);

        return v;
    }

    /** Returns height of a node in a tree or 0 if node is null. */
    private int height(@Nullable Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /** Returns size of a node in a tree or 0 if node is null. */
    private int size(@Nullable Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    /** Updates size and height of a node using children information. */
    private void update(@NotNull Node node) {
        node.size = size(node.left) + size(node.right) + 1;
        node.height = Integer.max(height(node.left), height(node.right)) + 1;
    }
}