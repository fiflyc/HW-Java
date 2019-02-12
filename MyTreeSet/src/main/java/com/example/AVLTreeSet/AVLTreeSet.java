package com.example.AVLTreeSet;

import com.example.MyTreeSet.MyTreeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.NoSuchElementException;

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
        @Nullable private Node<T> left;

        /** Right child. */
        @Nullable private Node<T> right;

        /** A node with next in order value. */
        @Nullable private Node<T> next;

        /** A node with previous in order value. */
        @Nullable private Node<T> prev;

        Node(@NotNull T value) {
            this.value = value;
            left = null;
            right = null;
            next = null;
            prev = null;
            height = 1;
            size = 1;
        }
    }

    /** Iterator. */
    private class Iterator<T> implements java.util.Iterator<T> {

        @NotNull private AVLTreeSet<T> treeSet;

        @Nullable private AVLTreeSet<T>.Node<T> currentNode;

        private boolean isReversed;

        private int version;

        private Iterator(@NotNull AVLTreeSet<T> treeSet, boolean isReversed) {
            this.isReversed = isReversed;
            this.treeSet = treeSet;
            version = treeSet.version;

            if (this.isReversed) {
                currentNode = treeSet.lastNode();
            } else {
                currentNode = treeSet.firstNode();
            }
        }

        /**
         * Returns true if the next element exists.
         * @throws IllegalStateException if an iterator is invalid
         */
        @Override
        public boolean hasNext() {
            if (version != treeSet.version) {
                throw new IllegalStateException();
            }

            return currentNode != null;
        }

        /**
         * Returns the next element in an iteration.
         * @throws IllegalStateException if an iterator is invalid
         * @throws NoSuchElementException if an iteration has no elements
         */
        @Override
        public T next() {
            if (version != treeSet.version) {
                throw new IllegalStateException();
            }
            if (currentNode == null) {
                throw new NoSuchElementException();
            }

            T result = currentNode.value;
            if (isReversed ^ treeSet.isReversed) {
                currentNode = currentNode.prev;
            } else {
                currentNode = currentNode.next;
            }

            return result;
        }
    }
    /** Root of a tree */
    @Nullable private Node<T> root;

    /** An order of elements. */
    boolean isReversed;

    /**
     * Compares elements.
     * If type T or his parent does not implement Comparable<>, AVLTreeSet will no work.
     */
    @Nullable private Comparator<? super T> comparator;

    /** Version of a tree.
     * Needs for disability iterators.
     */
    private int version;

    public AVLTreeSet() {
        root = null;
        comparator = null;
        isReversed = false;
        version = 0;
    }

    public AVLTreeSet(@NotNull Comparator<? super T> comparator) {
        root = null;
        this.comparator = comparator;
        isReversed = false;
        version = 0;
    }

    private AVLTreeSet(@NotNull AVLTreeSet<T> treeSet, boolean isReversed) {
        root = treeSet.root;
        comparator = treeSet.comparator;
        if (isReversed) {
            this.isReversed = !treeSet.isReversed;
        } else {
            this.isReversed = treeSet.isReversed;
        }
        version = 0;
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
        version++;

        root = add(root, element);

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
        version++;

        root = remove(root, (T) element);

        return false;
    }

    /**
     * Checks an element availability.
     * @throws IllegalArgumentException if an element is null
     * @return true if an element in a container
     */
    public boolean contains(@NotNull Object element) {
        return find((T) element) != null;
    }

    /** Returns number of elements in container. */
    @Override
    public int size() {
        return size(root);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>(this, false);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new Iterator<>(this, true);
    }

    /** Makes a new AVLTreeSet with reversed order of elements without copying data. */
    @Override
    public MyTreeSet<T> descendingSet() {
        return new AVLTreeSet<T>(this, true);
    }

    /** Returns the first element by an order. */
    @Override
    public T first() {
        var result = firstNode();
        if (result != null) {
            return result.value;
        } else {
            return null;
        }
    }

    /** Returns the last element by an order. */
    @Override
    public T last() {
        var result = lastNode();
        if (result != null) {
            return result.value;
        } else {
            return null;
        }
    }

    /**
     * Returns the greatest element lower than the given one.
     * @throws IllegalArgumentException if an element is null
     * */
    @Override
    public T lower(@NotNull T element) {
        var result = lower(root, element);
        if (result != null) {
            return result.value;
        } else {
            return null;
        }
    }

    /**
     * Returns the greatest element lower or equal than the given one.
     * @throws IllegalArgumentException if an element is null
     * */
    @Override
    public T floor(@NotNull T element) {
        if (contains(element)) {
            return element;
        } else {
            return lower(element);
        }
    }

    /**
     * Returns the least element greater or equal than the given one.
     * @throws IllegalArgumentException if an element is null
     * */
    @Override
    public T ceiling(@NotNull T element) {
        if (contains(element)) {
            return element;
        } else {
            return higher(element);
        }
    }

    /**
     * Returns the least element greater than the given one.
     * @throws IllegalArgumentException if an element is null
     * */
    @Override
    public T higher(@NotNull T element) {
        var result = higher(root, element);
        if (result != null) {
            return result.value;
        } else {
            return null;
        }
    }

    /**
     * Compares two elements.
     * Doesn't compile if x and y are incomparable.
     * @param x first element with type super T
     * @param y second element with type T
     * @throws IllegalArgumentException if one of elements is null
     * @return a result of comparing: positive number if x > y, negative if x < y or zero if x and y are equal
     */
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
    private Node<T> add(@Nullable Node<T> node, @NotNull T value) {
        if (node == null) {
            node = new Node<T>(value);

            node.prev = lower(root, value);
            if (node.prev != null) {
                node.prev.next = node;
            }
            node.next = higher(root, value);
            if (node.next != null) {
                node.next.prev = node;
            }

            return node;
        } else if (compare(node.value, value) > 0) {
            node.left = add(node.left, value);
        } else {
            node.right = add(node.right, value);
        }

        update(node);

        return balance(node);
    }

    /**
     * Removes a value from a subtree.
     * @param node -- the root of a subtree
     * @return the root of a new subtree
     */
    private Node<T> remove(@NotNull Node<T> node, @NotNull T value) {
        if (compare(node.value, value) < 0) {
            node.right = remove(node.right, value);
        } else if (compare(node.value, value) > 0) {
            node.left = remove(node.left, value);
        } else {
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }

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

        if (node == null) {
            return null;
        }

        update(node);

        return balance(node);
    }

    /** Balances a tree. */
    private Node<T> balance(@Nullable Node<T> node) {
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

    /** Left rotation. */
    private Node<T> rotateLeft(@NotNull Node<T> node) {
        Node<T> v = node.right;

        node.right = v.left;
        v.left = node;
        update(node);
        update(v);

        return v;
    }

    /** Right rotation. */
    private Node<T> rotateRight(@NotNull Node<T> node) {
        Node<T> v = node.left;

        node.left = v.right;
        v.right = node;
        update(node);
        update(v);

        return v;
    }

    /** Returns height of a node in a tree or 0 if node is null. */
    private int height(@Nullable Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /** Returns size of a node in a tree or 0 if node is null. */
    private int size(@Nullable Node<T> node) {
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

    /**
     * Returns the greatest element lower than the given one in a subtree.
     * @param node -- the root of a subtree
     */
    private Node<T> lower(@Nullable Node<T> node, @NotNull T element) {
        if (node == null) {
            return null;
        }

        if (compare(node.value, element) == 0) {
            return node.prev;
        } else if (compare(node.value, element) > 0) {
            return lower(node.left, element);
        } else {
            Node<T> result = lower(node.right, element);
            if (result == null) {
                return node;
            } else {
                return result;
            }
        }
    }

    /**
     * Returns the least element greater than the given one in a subtree.
     * @param node -- the root of a subtree
     */
    private Node<T> higher(@Nullable Node<T> node, @NotNull T element) {
        if (node == null) {
            return null;
        }

        if (compare(node.value, element) == 0) {
            return node.next;
        } else if (compare(node.value, element) < 0) {
            return higher(node.right, element);
        } else {
            Node<T> result = higher(node.left, element);
            if (result == null) {
                return node;
            } else {
                return result;
            }
        }
    }

    /** Finds a node contains matching value. */
    private Node<T> find(@NotNull T value) {
        Node<T> current = root;

        while (current != null) {
            if (compare(value, current.value) < 0) {
                current = current.left;
            } else if (compare(value, current.value) > 0) {
                current = current.right;
            } else {
                return current;
            }
        }

        return null;
    }

    /** Returns a node with the first element by an order. */
    private Node<T> firstNode() {
        if (root == null) {
            return null;
        }

        Node<T> current = root;
        if (isReversed) {
            while (current.right != null) {
                current = current.right;
            }
        } else {
            while (current.left != null) {
                current = current.left;
            }
        }

        return current;
    }

    /** Returns a node with the last element by an order. */
    private Node<T> lastNode() {
        if (root == null) {
            return null;
        }

        Node<T> current = root;
        if (isReversed) {
            while (current.left != null) {
                current = current.left;
            }
        } else {
            while (current.right != null) {
                current = current.right;
            }
        }


        return current;
    }
}