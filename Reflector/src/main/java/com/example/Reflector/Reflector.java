package com.example.Reflector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;

/**
 * Contains static methods for generating .java file with class definition.
 */
public class Reflector {

    /**
     * Prints structure of a class.
     * @param someClass class for printing
     * @throws IOException
     */
    public static void printStructure(Class<?> someClass) throws IOException {
        File file = new File("." + File.separator, someClass.getName() + ".java");
        try (var fileOutputStream = new FileOutputStream(file)) {
            var writer = new OutputStreamWriter(fileOutputStream);

            printClass(someClass, writer, "");
        }
    }

    /**
     * Prints structure of a class.
     * @param someClass class for printing
     * @param writer output writer
     * @param prefix prefix of each line of output
     * @throws IOException
     */
    public static void printClass(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        printClassHeading(someClass, writer, prefix);
        writer.write(" {\n");

        for (var innerClass: someClass.getDeclaredClasses()) {
            printClass(innerClass, writer, prefix + "\t");
        }

        printAllFields(someClass, writer, prefix + "\t");
        printAllMethods(someClass, writer, prefix + "\t");
        writer.write(prefix + "}\n");

        writer.flush();
    }

    /**
     * Prints heading of a class (modifiers, class name, parent class and implemented interfaces).
     * Also prints all generic types.
     * @param someClass class for printing
     * @param writer output writer
     * @throws IOException
     */
    private static void printClassHeading(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        writer.write(prefix);
        writer.write(someClass.toGenericString().replace('$', '.'));

        if (someClass.getSuperclass() != Object.class) {
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
    private static void printAllFields(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        for (var field: someClass.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            writer.write(prefix);
            if (field.getModifiers() != 0) {
                writer.write(Modifier.toString(field.getModifiers()));
                writer.write(" ");
            }

            writer.write(field.getGenericType().getTypeName().replace('$', '.') + " ");
            writer.write(field.getName() + ";\n");
        }
    }

    /**
     * Prints all methods of class (private, public etc) with generic types.
     * Also prints all throwing exception.
     * @param someClass class for printing
     * @param writer output writer
     * @throws IOException
     */
    private static void printAllMethods(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        for (var method: someClass.getDeclaredMethods()) {
            if (method.isSynthetic()) {
                continue;
            }

            writer.write(prefix);
            if (method.getModifiers() != 0) {
                writer.write(Modifier.toString(method.getModifiers()) + " ");
            }

            writer.write(method.getGenericReturnType().getTypeName().replace('$', '.') + " ");
            writer.write(method.getName() + "(");

            if (method.getGenericParameterTypes().length > 0) {
                var parameters = method.getGenericParameterTypes();
                for (int i = 0; i < parameters.length - 1; i++) {
                    writer.write(parameters[i].getTypeName().replace('$', '.') + " ");
                }
                writer.write(parameters[parameters.length - 1].getTypeName().replace('$', '.'));
            }
            writer.write(")");

            if (method.getGenericExceptionTypes().length > 0) {
                var exceptions = method.getGenericExceptionTypes();
                for (int i = 0; i < exceptions.length - 1; i++) {
                    writer.write(exceptions[i].getTypeName().replace('$', '.') + " ");
                }
                writer.write(exceptions[exceptions.length - 1].getTypeName().replace('$', '.'));
            }

            if (Modifier.isAbstract(method.getModifiers())) {
                writer.write(";\n");
            } else if (method.getReturnType() == void.class) {
                writer.write(" {\n" + prefix + "\treturn;\n" + prefix + "}\n");
            } else if (method.getReturnType() == Boolean.class || method.getReturnType() == boolean.class) {
                writer.write(" {\n"+ prefix + "\treturn false;\n" + prefix + "}\n");
            } else if (method.getReturnType() == Character.class || method.getReturnType() == char.class) {
                writer.write(" {\n" + prefix + "\treturn '\0';\n" + prefix + "}\n");
            } else if (method.getReturnType().isPrimitive()) {
                writer.write(" {\n" + prefix + "\treturn 0;\n" + prefix + "}\n");
            } else {
                writer.write(" {\n" + prefix + "\treturn null;\n" + prefix + "}\n");
            }
        }
    }
}