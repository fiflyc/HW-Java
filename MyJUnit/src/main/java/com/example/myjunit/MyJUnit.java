package com.example.myjunit;

import org.junit.*;

import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyJUnit {

    /** Thread Pool what takes tasks "run testes". */
    private static ExecutorService threadPool;

    /** Get directory with testes for executing. */
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }

        threadPool = Executors.newFixedThreadPool(30);
        File dir = new File(args[0]);
        if (dir.isFile()) {
            runTestsFromFile(dir);
        } else {
            for (var file: dir.listFiles()) {
                runTestsFromFile(file);
            }
        }
    }

    /** Run testes from file. */
    public static void runTestsFromFile(File file) {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(30);
        }

        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null,
                "-d",
                "./out/production/classes/com/example/myjunit",
                file.getAbsolutePath());

        try {
            var loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            Class<?> cl = Class.forName(file.getAbsolutePath(), true, loader);

            var beforeAll = new ArrayList<Method>();
            var afterAll = new ArrayList<Method>();
            var beforeEach = new ArrayList<Method>();
            var afterEach = new ArrayList<Method>();
            var testes = new ArrayList<Method>();
            var ignored = new HashSet<Method>();

            for (var method: cl.getMethods()) {
                for (var annotation: method.getDeclaredAnnotations()) {
                    if (annotation.getClass().equals(BeforeClass.class)) {
                        beforeAll.add(method);
                        break;
                    } else if (annotation.getClass().equals(AfterClass.class)) {
                        afterAll.add(method);
                        break;
                    } else if (annotation.getClass().equals(Before.class)) {
                        beforeEach.add(method);
                        break;
                    } else if (annotation.getClass().equals(After.class)) {
                        afterEach.add(method);
                        break;
                    } else if (annotation.getClass().equals(Test.class)) {
                        testes.add(method);
                        break;
                    } else if (annotation.getClass().equals(Ignore.class)) {
                        System.out.println("Ignore test " + method.getName() + ": " + annotation.toString());
                        ignored.add(method);
                        break;
                    }
                }
            }

            executeTestes(beforeAll, null, null, ignored);
            executeTestes(testes, beforeEach, afterEach, ignored);
            executeTestes(afterAll, null, null, ignored);
        } catch (MalformedURLException e) {
            System.out.println("Failed while creating class loader");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed while loading class from file " + file.getAbsolutePath());
        }
    }

    /**
     * Execute testes in thread Pool.
     * Locks until they executing.
     * @param testes testes for executing
     * @param beforeEach testes executing before each testes
     * @param afterEach testes executing after each testes
     * @param ignored testes what would been ignored
     */
    private static void executeTestes(ArrayList<Method> testes, ArrayList<Method> beforeEach,
                                      ArrayList<Method> afterEach, HashSet<Method> ignored) {
        var latch = new CountDownLatch(testes.size());

        for (var test: testes) {
            threadPool.submit(() -> {
                try {
                    for (var before: beforeEach) {
                        if (!ignored.contains(before)) {
                            before.invoke(new Object[] {});
                        }
                    }
                    if (!ignored.contains(test)) {
                        test.invoke(new Object[]{});
                    }
                    for (var after: afterEach) {
                        if (!ignored.contains(after)) {
                            after.invoke(new Object[] {});
                        }
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("Illegal access for the test " + test.getName());
                } catch (InvocationTargetException e) {
                    System.out.println("Test " + test.getName() + "has some arguments");
                }

                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            /* Do nothing. */
        }
    }
}