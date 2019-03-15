package com.example.QSort;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QSort {

    public static void main(String[] args) {

    }

    public static <E extends Comparable<? super E>> void sort(@NotNull ArrayList<E> array, int first, int last) {
        if (first < 0 || last >= array.size() || first >= last) {
            return;
        }

        int middle = partition(array, first, last);
        sort(array, first, middle);
        sort(array, middle + 1, last);
    }

    public static <E extends Comparable<? super E>> void parallelSort(@NotNull ArrayList<E> array, int first, int last, int threads) {
        if (threads <= 1) {
            sort(array, first, last);
            return;
        }

        int middle = partition(array, first, last);
        Thread thread1 = new Thread(() -> QSort.parallelSort(array, first, middle, threads - 2));
        Thread thread2 = new Thread(() -> QSort.parallelSort(array, middle + 1, last, threads - 2));
        thread1.start();
        thread2.start();
    }

    private static <E extends Comparable<? super E>> int partition(@NotNull ArrayList<E> array, int first, int last) {
        if (first < 0 || last >= array.size() || first > last) {
            return -1;
        }
        if (first == last) {
            return first;
        }

        int middle = first;
        for (int i = middle + 1; i <= last; i++) {
            if (array.get(i).compareTo(array.get(middle)) < 0) {
                E temp = array.get(middle);
                array.set(middle, array.get(i));
                array.set(i, temp);
                middle++;
            }
        }

        return middle;
    }
}