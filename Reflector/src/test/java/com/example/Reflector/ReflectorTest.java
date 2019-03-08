package com.example.Reflector;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.example.Reflector.TestClasses.*;

import java.io.*;

import static com.intellij.util.indexing.impl.DebugAssertions.assertTrue;

class ReflectorTest {

    @Test
    void printStructure_AbstractClass_CorrectModifiersAndBraces() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test1.out"));

        Reflector.printClass(AbstractClass.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test1.out"),
                new File("./src/test/resources/test1.expected")));
    }

    @Test
    void printStructure_ClassImplementsOneInterface_CorrectClassHeading() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test2.out"));

        Reflector.printClass(ClassImplementsOneInterface.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test2.out"),
                new File("./src/test/resources/test2.expected")));
    }

    @Test
    void printStructure_ClassImplementsTwoInterfaces_CorrectClassHeading() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test3.out"));

        Reflector.printClass(ClassImplementsTwoInterfaces.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test3.out"),
                new File("./src/test/resources/test3.expected")));
    }

    @Test
    void printStructure_ClassWithInnerClass_CorrectInnerClass() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test4.out"));

        Reflector.printClass(ClassWithInnerClass.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test4.out"),
                new File("./src/test/resources/test4.expected")));
    }

    @Test
    void printStructure_ClassWithNestedClass_CorrectNestedClass() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test5.out"));

        Reflector.printClass(ClassWithNestedClass.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test5.out"),
                new File("./src/test/resources/test5.expected")));
    }

    @Test
    void printStructure_ClassWithParent_CorrectClassHeading() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test6.out"));

        Reflector.printClass(ClassWithParent.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test6.out"),
                new File("./src/test/resources/test6.expected")));
    }

    @Test
    void printStructure_ClassWithSomeMethods_CorrectSetOfMethods() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test7.out"));

        Reflector.printClass(ClassWithSomeMethods.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test7.out"),
                new File("./src/test/resources/test7.expected")));
    }

    @Test
    void printStructure_ClassWithSomeFields_CorrectSetOfFields() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test8.out"));

        Reflector.printClass(ClassWithSomeFields.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test8.out"),
                new File("./src/test/resources/test8.expected")));
    }

    @Test
    void printStructure_GenericClass_GenericTypePrinted() throws IOException {
        var writer = new OutputStreamWriter(new FileOutputStream("./src/test/resources/test9.out"));

        Reflector.printClass(GenericClass.class, writer, "");

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test9.out"),
                new File("./src/test/resources/test9.expected")));
    }
}