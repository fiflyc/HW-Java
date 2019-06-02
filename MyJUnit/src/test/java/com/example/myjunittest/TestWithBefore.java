package com.example.myjunittest;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TestWithBefore {

    @BeforeClass
    public void beforeTest() throws IOException {
        var out = new OutputStreamWriter(new FileOutputStream("./src/test/resources/BeforeClass.out"));
        out.write(1);
    }

    @Test
    public void test() {
        /* Do something. */
    }
}