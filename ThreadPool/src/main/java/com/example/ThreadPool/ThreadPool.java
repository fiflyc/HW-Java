package com.example.ThreadPool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

/** Thread Pool. */
public class ThreadPool {

    private class MyFuture<R> implements LightFuture<R> {

        /** Readiness of a task result. */
        private volatile boolean isReady;

        /** A result of executing. */
        private volatile R result;

        /** A mutex for task ending informing. */
        private final Object taskReadyMutex;

        MyFuture() {
            isReady = false;
            result = null;
            taskReadyMutex = new Object();
        }

        @Override
        public boolean isReady() {
            synchronized (taskReadyMutex) {
                return isReady;
            }
        }

        @Override
        public R get() throws LightExecutionException {
            synchronized (taskReadyMutex) {
                while (result == null) {
                    try {
                        taskReadyMutex.wait();
                    } catch (InterruptedException e) {
                        // do nothing
                    } catch (Exception e) {
                        throw new LightExecutionException(e.getMessage());
                    }
                }

                return result;
            }
        }

        @Override
        public <T> LightFuture<T> thenApply(Function<R, T> function) {
            synchronized (taskReadyMutex) {
                while (result == null) {
                    try {
                        taskReadyMutex.wait();
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }

                return ThreadPool.this.submit(() -> function.apply(result));
            }
        }
    }

    /** All working threads. */
    private ArrayList<Thread> threads;

    /** A new task for execution. */
    @Nullable private MyFuture newFuture;

    /** A new LightFuture object corresponding a new task. */
    @Nullable private Supplier newTask;

    /** A mutex for threads. */
    private final Object threadsMutex;

    /** A mutex for task sender (submit() method). */
    private final Object submitMutex;

    /**
     * Creates a new thread pool.
     * @param n number of working threads
     */
    public ThreadPool(int n) {
        threadsMutex = new Object();
        submitMutex = new Object();
        threads = new ArrayList<>();
        newFuture = null;
        newTask = null;

        for (int i = 0; i < n; i++) {
            threads.add(new Thread(() -> {
                while (true) {
                    Supplier task;
                    MyFuture future;

                    synchronized (threadsMutex) {
                        while (newTask == null || newFuture == null) {
                            try {
                                threadsMutex.wait();
                            } catch (InterruptedException e) {
                                return;
                            }
                        }

                        task = newTask;
                        future = newFuture;
                        newTask = null;
                        newFuture = null;
                    }

                    synchronized (submitMutex) {
                        submitMutex.notify();
                    }

                    synchronized (future.taskReadyMutex) {
                        future.result = task.get();
                        future.isReady = true;

                        future.taskReadyMutex.notify();
                    }
                }
            }));

            threads.get(i).start();
        }
    }

    /**
     * Adds a new task in a thread pool.
     * Undefined behavior if send task after calling shutdown().
     * @param task task for execution
     * @return a LightFuture object with an information about task execution
     */
    @NotNull public <R> LightFuture<R> submit(@NotNull Supplier<R> task) {
        var future = new MyFuture<R>();

        synchronized (submitMutex) {
            while (newTask != null || newFuture != null) {
                try {
                    submitMutex.wait();
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            newTask = task;
            newFuture = future;
        }

        synchronized (threadsMutex) {
            threadsMutex.notify();
        }

        return future;
    }

    void shutdown() {
        for (var thread: threads) {
            thread.interrupt();
        }
    }
}
