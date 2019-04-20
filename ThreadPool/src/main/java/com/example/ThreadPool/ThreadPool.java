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
    @Nullable private Supplier newTask;

    /** A mutex for newTask. */
    private final Object taskMutex;

    /** A LightFuture object with information about the new task. */
    @Nullable private MyFuture newFuture;

    /** A mutex for newFuture. */
    private final Object futureMutex;

    /**
     * Creates a new thread pool.
     * @param n number of working threads
     */
    public ThreadPool(int n) {
        taskMutex = new Object();
        futureMutex = new Object();
        threads = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            threads.add(new Thread(() -> {
                while (true) {
                    Supplier task;
                    MyFuture future;

                    synchronized (taskMutex) {
                        while (newTask == null) {
                            try {
                                taskMutex.wait();
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                        task = newTask;
                        newTask = null;
                        taskMutex.notify();

                        synchronized (futureMutex) {
                            while (newFuture != null) {
                                try {
                                    futureMutex.wait();
                                } catch (InterruptedException e) {
                                    return;
                                }
                            }

                            newFuture = new MyFuture();
                            future = newFuture;
                        }
                        futureMutex.notify();
                    }

                    Object result = task.get();
                    synchronized (future.taskReadyMutex) {
                        future.result = result;
                        future.isReady = true;
                        future.taskReadyMutex.notifyAll();
                    }
                }
            }));
        }
    }

    /**
     * Adds a new task in a thread pool.
     * @param task task for execution
     * @return a LightFuture object with an information about task execution
     */
    @NotNull public <R> LightFuture<R> submit(@NotNull Supplier<R> task) {
        synchronized (taskMutex) {
            while (newTask != null) {
                try {
                    taskMutex.wait();
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            newTask = task;
        }
        taskMutex.notify();

        LightFuture result;
        synchronized (futureMutex) {
            while (newFuture == null) {
                try {
                    futureMutex.wait();
                } catch (InterruptedException e) {
                    // do nothing
                }
            }

            result = newFuture;
            newFuture = null;
        }
        futureMutex.notify();

        return result;
    }
}
