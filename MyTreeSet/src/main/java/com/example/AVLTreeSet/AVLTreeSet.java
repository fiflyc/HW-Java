package com.example.AVLTreeSet;

import com.example.MyTreeSet.MyTreeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

public class AVLTreeSet<T> extends AbstractSet<T> implements MyTreeSet<T> {

    private class Node<T> {

        @NotNull private T value;

        private int height;

        private int size;

        @Nullable private Node left;

        @Nullable private Node right;
    }

    @Nullable private Node root;

    @Nullable private Comparator<? super T> comparator;

    AVLTreeSet() {
        root = null;
        comparator = null;
    }

    AVLTreeSet(@NotNull Comparator<? super T> comparator) {
        root = null;
        this.comparator = comparator;
    }

    public boolean add(@NotNull T element) {

        return false;
    }

    public boolean remove(@NotNull Object element) {

        return false;
    }

    public boolean contains(@NotNull Object element) {

        return false;
    }

    @Override
    public int size() {
        return 0;
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

    private Node add(@NotNull Node node, T value) {
        return null;
    }

    private Node remove(@NotNull Node node, T value) {
        return null;
    }

    private Node balance(@NotNull Node node) {
        return null;
    }

    private Node rotateLeft(@NotNull Node node) {
        return null;
    }

    private Node rotateRight(@NotNull Node node) {
        return null;
    }

    private int height(@Nullable Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int size(@Nullable Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    private void update(@NotNull Node node) {
        node.size = size(node.left) + size(node.right) + 1;
        node.height = Integer.max(height(node.left), height(node.right)) + 1;
    }
}