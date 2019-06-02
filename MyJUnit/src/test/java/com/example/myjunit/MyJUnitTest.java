package com.example.myjunit;

import org.junit.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MyJUnitTest {

    @Test
    public void runSimpleTest() {
        var file = new File("src/test/java/com/example/myjunittest/SimpleTest.java");
        assertEquals(1, MyJUnit.runTestsFromFile(file));
    }

    @Test
    public void runFallenTest() {
        var file = new File("src/test/java/com/example/myjunittest/SimpleTest.java");
        assertEquals(0, MyJUnit.runTestsFromFile(file));
    }

    @Test
    public void runTestWithBefore() throws IOException {
        var file = new File("src/test/java/com/example/myjunittest/TestWithBefore.java");
        MyJUnit.runTestsFromFile(file);
        var in = new InputStreamReader(new FileInputStream("src/test/resources/BeforeClass.out"));
        int input = in.read();
        assertEquals(1, input);
    }

    @Test
    public void runTestWithAfter() throws IOException {
        var file = new File("src/test/java/com/example/myjunittest/TestWithAfter.java");
        MyJUnit.runTestsFromFile(file);
        var in = new InputStreamReader(new FileInputStream("src/test/resources/AfterClass.out"));
        int input = in.read();
        assertEquals(1, input);
    }
}