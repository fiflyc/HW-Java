package com.example.QSort;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class QSort {

    public static void main(String[] args) {
        var array = new ArrayList<Integer>();
        array.add(0);
        array.add(-1);
        var random = new Random();

        long sortTime = checkSortTime(array);
        long parallelSortTime = checkParallelSortTime(array);
        while (parallelSortTime >= sortTime) {
            for (int i = 0; i < array.size() / 2; i++) {
                array.add(random.nextInt());
            }
            sortTime = checkSortTime(array);
            parallelSortTime = checkParallelSortTime(array);
        }

        System.out.println("When array size is " + array.size() + " threads win.");
    }

    private static <E extends Comparable<? super E>> long checkSortTime(@NotNull ArrayList<E> array) {
        long start = System.nanoTime();
        sort(array, 0, array.size() - 1);
        long finish = System.nanoTime();
        return finish - start;
    }

    private static <E extends Comparable<? super E>> long checkParallelSortTime(@NotNull ArrayList<E> array) {
        long start = System.nanoTime();
        parallelSort(array, 0, array.size() - 1, 8);
        long finish = System.nanoTime();
        return finish - start;
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