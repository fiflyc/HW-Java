package com.example.Reflector;

import com.example.Reflector.TestClass.TestClass;
import com.google.common.base.Defaults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;

public class Reflector {

    public static void main(String[] args) throws IOException {
        printStructure(TestClass.class);
    }

    public static void printStructure(Class<?> someClass) throws IOException {
        File file = new File("." + File.separator, someClass.getName() + ".java");
        try (var fileOutputStream = new FileOutputStream(file)) {
            var writer = new OutputStreamWriter(fileOutputStream);

            printClassHeading(someClass, writer);
            writer.write(" {\n\n");
            printAllFields(someClass, writer);
            writer.write("\n");
            printAllMethods(someClass, writer, Modifier.isAbstract(someClass.getModifiers()));
            writer.write("}\n");

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
        for (var field: someClass.getDeclaredFields()) {
            writer.write("\t");
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
    private static void printAllMethods(Class<?> someClass, OutputStreamWriter writer, boolean isAbstract) throws IOException {
        for (var method: someClass.getDeclaredMethods()) {
            writer.write("\t");
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

            if (isAbstract) {
                writer.write(";\n");
            } else if (method.getReturnType() == null) {
                writer.write(" {\n\t\treturn;\n\t}\n");
            } else if (method.getReturnType() == Boolean.class || method.getReturnType() == boolean.class) {
                writer.write(" {\n\t\treturn false;\n\t}\n");
            } else if (method.getReturnType() == Character.class || method.getReturnType() == char.class) {
                writer.write(" {\n\t\treturn '\0';\n\t}\n");
            } else if (method.getReturnType().isPrimitive()) {
                writer.write(" {\n\t\treturn 0;\n\t}\n");
            } else {
                writer.write(" {\n\t\treturn null;\n\t}\n");
            }
        }
    }
}