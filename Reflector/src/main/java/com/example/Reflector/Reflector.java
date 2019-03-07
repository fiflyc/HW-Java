package com.example.Reflector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;

public class Reflector {

    public static void printStructure(Class<?> someClass) throws IOException {
        File file = new File("." + File.separator, someClass.getName() + ".java");
        try (var fileOutputStream = new FileOutputStream(file)) {
            var writer = new OutputStreamWriter(fileOutputStream);

            printClassHeading(someClass, writer);
            writer.write(" {\n\n");
            printAllFields(someClass, writer);
            writer.flush();
        }
    }

    /**
     * Prints heading of a class (modifiers, class name, parent class and implemented interfaces).
     * Also prints all generic types.
     * @param someClass class for printing
     * @param writer output writer
     * @throws IOException
     */
    private static void printClassHeading(Class<?> someClass, OutputStreamWriter writer) throws IOException {
        writer.write(someClass.toGenericString().replace('$', '.'));

        if (someClass.getSuperclass() != null) {
            writer.write(" extends ");
            writer.write(someClass.getSuperclass().getCanonicalName().replace('$', '.'));
        }

        if (someClass.getInterfaces().length > 0) {
            writer.write(" implements ");

            var interfaces = someClass.getInterfaces();
            for (int i = 0; i < interfaces.length - 1; i++) {
                writer.write(interfaces[i].getCanonicalName() + ", ");
            }
            writer.write(interfaces[interfaces.length - 1].getCanonicalName());
        }
    }

    /**
     * Prints all fields of class (private, public, etc.).
     * Also prints all generic types.
     * @param someClass class for printing
     * @param writer output writer
     * @throws IOException
     */
    private static void printAllFields(Class<?> someClass, OutputStreamWriter writer) throws IOException {
        for (var current: someClass.getDeclaredFields()) {
            writer.write("\t");
            if (current.getModifiers() != 0) {
                writer.write(Modifier.toString(current.getModifiers()));
                writer.write(" ");
            }
            writer.write(current.getGenericType().getTypeName().replace('$', '.'));
            writer.write(" ");
            writer.write(current.getName());
            writer.write(";\n");
        }
    }
}