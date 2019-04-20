package com.example.ThreadPool;

import java.util.function.Supplier;

public class ThreadPool {

    /**
     * Creates a new thread pool.
     * @param n number of working threads
     */
    public ThreadPool(int n) {

    }

    /**
     * Adds a new task in a thread pool.
     * @param task task for execution
     * @return a LightFuture object with an information about task execution
     */
    public <R> LightFuture<R> submit(Supplier<R> task) {
        return null;
    }
}
