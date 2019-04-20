package com.example.ThreadPool;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/** Interface, containing information about an executing task. */
public interface LightFuture<R> {

    /** Returns true if a task result is ready. */
    boolean isReady();

    /**
     * Returns a result of an executed task.
     * If the result has not been executed yet, get() waits it.
     * @throws LightExecutionException if the task fallen by any exception
     */
    @NotNull R get() throws LightExecutionException;

    /**
     * Applies a function to a result of an executing task.
     * @param function a function for application
     * @return a new task executing the function
     */
    @NotNull <T> LightFuture<T> thenApply(Function<R, T> function);
}
