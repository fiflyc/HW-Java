package com.example.threadpool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {
    private ThreadPool threadPool;

    @BeforeEach
    void initialize() {
        threadPool = new ThreadPool(30);
    }

    @AfterEach
    void shutdown() {
        threadPool.forceShutdown();
    }

    @Test
    void threadPool_SeveralThreads_TasksExecuted() throws LightExecutionException {
        var results = new ArrayList<LightFuture<Integer>>();
        for (int i = 0; i < 100; i++) {
            int j = i;
            results.add(threadPool.submit(() -> j));
        }

        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(i), results.get(i).get());
        }
    }

    @Test
    void thenApply_Function_NewTaskExecuted() throws LightExecutionException {
        var future = threadPool.submit(() -> 1);
        future = future.thenApply((x) -> x * 10);

        assertEquals(Integer.valueOf(10), future.get());
    }

    @Test
    void submit_FallingTask_LightExecutionException() {
        var future = threadPool.submit((Supplier<Integer>) () -> {
            throw new NullPointerException();
        });

        assertThrows(LightExecutionException.class, future::get);
    }

    @Test
    void isReady_AfterTaskExecution_True() throws LightExecutionException {
        var future = threadPool.submit(() -> 1);
        future.get();

        assertTrue(future.isReady());
    }

    @Test
    void submit_AfterShutdown_IllegalStateException() {
        threadPool.forceShutdown();

        assertThrows(IllegalStateException.class, () -> threadPool.submit(() -> 1));
    }

    @Test
    void threadPool_ThirtyThreads_AllThreadsCanWork() throws LightExecutionException {
        var numberOfThreads = new AtomicInteger(0);

        for (int i = 0; i < 30; i++) {
            int j = i;
            threadPool.submit(() -> {
                numberOfThreads.getAndIncrement();

                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e) {
                    // do nothing
                }

                return 1;
            });
        }

        while (numberOfThreads.intValue() < 30) {
            // im sorry for this:(
        }
    }
}