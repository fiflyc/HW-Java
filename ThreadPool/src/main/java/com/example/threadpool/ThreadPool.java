package com.example.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

/** Thread Pool. */
public class ThreadPool {

    /** A task for executing. */
    private static class Task<T, R> {

        private class MyFuture implements LightFuture<R> {

            /** Readiness of a task result. */
            private boolean isReady;

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
                return isReady;
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
            public <E> LightFuture<E> thenApply(@NotNull Function<? super R, E> function) {
                synchronized (taskReadyMutex) {
                    Task.this.nextTask = new Task<>(function, Task.this.queue);
                    if (result != null) {
                        Task.this.nextTask.argument = result;
                        Task.this.nextTask.submit();
                    }
                }
                return (LightFuture<E>) nextTask.future;
            }
        }

        @Nullable private T argument;
        @NotNull private Function<? super T, R> function;
        @NotNull private MyFuture future;
        @Nullable private Task<R, ?> nextTask;

        /** For submitting myself to the ThreadPool. */
        @NotNull private final ArrayDeque<Task> queue;

        private Task(@NotNull Function<? super T, R> function, @NotNull ArrayDeque<Task> queue) {
            this.function = function;
            future = this.new MyFuture();
            this.queue = queue;
        }

        private void submit() {
            synchronized (queue) {
                queue.addLast(this);
                queue.notify();
            }
        }
    }

    /** All working threads. */
    private ArrayList<Thread> threads;

    /** True if shutdown() method was called. */
    private volatile Boolean isValid;

    /** New tasks for executing. */
    @NotNull private final ArrayDeque<Task> newTasks;

    /**
     * Creates a new thread pool.
     * @param n number of working threads
     */
    public ThreadPool(int n) {
        isValid = true;
        threads = new ArrayList<>();
        newTasks = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            threads.add(createNewThread());
            threads.get(i).start();
        }
    }

    /** Creates a new thread. */
    private Thread createNewThread() {
        return new Thread(() -> {
            while (true) {
                Task task;

                synchronized (newTasks) {
                    while (newTasks.size() == 0) {
                        try {
                            newTasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    task = newTasks.removeFirst();
                }

                synchronized (task.future.taskReadyMutex) {
                    try {
                        task.future.result = task.function.apply(task.argument);
                    } catch (Exception e) {
                        task.future.exception = e;
                    }
                    task.future.isReady = true;

                    task.future.taskReadyMutex.notify();
                }

                if (task.nextTask != null) {
                    task.nextTask.argument = task.future.result;

                    synchronized (newTasks) {
                        newTasks.addLast(task.nextTask);
                        newTasks.notify();
                    }
                } else {
                }
            }
        });
    }

    /**
     * Adds a new task in a thread pool.
     * @param task task for execution
     * @throws IllegalStateException if called after forceShutdown()
     * @return a LightFuture object with an information about task execution
     */
    @NotNull public <R> LightFuture<R> submit(@NotNull Supplier<R> task) throws IllegalStateException {
        if (!isValid) {
            throw new IllegalStateException("Thread Pool: all threads was interrupted, because forceShutdown() has been already called.");
        }

        var newTask = new Task<>((o) -> task.get(), newTasks);
        newTask.submit();

        return newTask.future;
    }

    /** Interrupts all threads in a thread pool. */
    public void forceShutdown() {
        for (var thread: threads) {
            thread.interrupt();
        }

        isValid = false;
    }
}
