package com.example.threadpool;

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

        /** Was a task fallen by exception. */
        @Nullable private volatile Exception exception;

        /** A result of executing. */
        @Nullable private volatile R result;

        /** A mutex for task ending informing. */
        private final Object taskReadyMutex;

        MyFuture() {
            isReady = false;
            exception = null;
            result = null;
            taskReadyMutex = new Object();
        }

        /** {@link LightFuture#isReady()} */
        @Override
        public boolean isReady() {
            synchronized (taskReadyMutex) {
                return isReady;
            }
        }

        /** {@link LightFuture#get()} */
        @Override
        public R get() throws LightExecutionException {
            synchronized (taskReadyMutex) {
                while (result == null) {
                    if (exception != null) {
                        throw new LightExecutionException(exception.getMessage());
                    }

                    try {
                        taskReadyMutex.wait();
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }

                return result;
            }
        }

        /** {@link LightFuture#thenApply(Function)} ()} */
        @Override
        public <T> LightFuture<T> thenApply(@NotNull Function<R, T> function) {
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

    /** True if shutdown() method was called. */
    private volatile Boolean isValid;

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
        isValid = true;
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
                        try {
                            future.result = task.get();
                        } catch (Exception e) {
                            future.exception = e;
                        }
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
     * @param task task for execution
     * @throws IllegalStateException if called after shutdown()
     * @return a LightFuture object with an information about task execution
     */
    @NotNull public <R> LightFuture<R> submit(@NotNull Supplier<R> task) throws IllegalStateException {
        synchronized (isValid) {
            if (!isValid) {
                throw new IllegalStateException("threadpool: all threads was interrupted, because shutdown() has been already called.");
            }
        }

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

    /** Interrupts all threads in a thread pool. */
    public void shutdown() {
        for (var thread: threads) {
            thread.interrupt();
        }

        synchronized (isValid) {
            isValid = false;
        }
    }
}
