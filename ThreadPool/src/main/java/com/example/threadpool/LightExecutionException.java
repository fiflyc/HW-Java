package com.example.threadpool;

/** An exception throwing in ThreadPool, if the task threw any exception. */
public class LightExecutionException extends Exception {

    LightExecutionException(String message) {
        super(message);
    }
}
