package com.example.Reflector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Contains static methods for generating .java file with class definition.
 */
public class Reflector {

    /**
     * Prints structure of a class.
     * @param someClass class for printing
     */
    public static void printStructure(Class<?> someClass) throws IOException {
        File file = new File("." + File.separator, someClass.getName() + ".java");
        try (var fileOutputStream = new FileOutputStream(file)) {
            var writer = new OutputStreamWriter(fileOutputStream);

            printClass(someClass, writer, "");
        }
    }

    /**
     * Prints different methods and fields of two classes.
     * @param a first class
     * @param b second class
     */
    public static void diffClasses(Class<?> a, Class<?> b) throws IOException {
        diffClasses(a, b, new OutputStreamWriter(System.out));
    }

    /**
     * Prints different methods and fields of two classes.
     * @param a first class
     * @param b second class
     * @param writer output writer
     */
    public static void diffClasses(Class<?> a, Class<?> b, OutputStreamWriter writer) throws IOException {
        diffFields(a, b, writer);
        diffMethods(a, b, writer);
        writer.flush();
    }

    /**
     * Prints different fields of two classes.
     * @param a first class
     * @param b second class
     * @param writer output writer
     */
    private static void diffFields(Class<?> a, Class<?> b, OutputStreamWriter writer) throws IOException {
        var fieldsA = new TreeMap<String, Field>();
        for (var field: a.getDeclaredFields()) {
            fieldsA.put(field.getName(), field);
        }
        var fieldsB = new TreeMap<String, Field>();
        for (var field: b.getDeclaredFields()) {
            fieldsB.put(field.getName(), field);
        }

        for (var field: b.getDeclaredFields()) {
            if (fieldsA.containsKey(field.getName())) {
                var fieldA = fieldsA.get(field.getName());

                if (isEqual(field, fieldA)) {
                    fieldsB.remove(field.getName());
                    fieldsA.remove(field.getName());
                }
            }
        }

        var stringBuilder = new StringBuilder();
        fieldsA.forEach((name, field) -> {
            stringBuilder.append(field.toGenericString()).append("\n");
        });
        fieldsB.forEach((name, field) -> {
            stringBuilder.append(field.toGenericString()).append("\n");
        });

        writer.write(stringBuilder.toString());
    }

    /**
     * Prints different methods of two classes.
     * @param a first class
     * @param b second class
     * @param writer output writer
     */
    private static void diffMethods(Class<?> a, Class<?> b, OutputStreamWriter writer) throws  IOException {
        var methodsA = new TreeMap<String, ArrayList<Method>>();
        for (var method: a.getDeclaredMethods()) {
            if (methodsA.containsKey(method.getName())) {
                methodsA.get(method.getName()).add(method);
            } else {
                methodsA.put(method.getName(), new ArrayList<>());
                methodsA.get(method.getName()).add(method);
            }
        }
        var methodsB = new TreeMap<String, ArrayList<Method>>();
        for (var method: b.getDeclaredMethods()) {
            if (methodsB.containsKey(method.getName())) {
                methodsB.get(method.getName()).add(method);
            } else {
                methodsB.put(method.getName(), new ArrayList<>());
                methodsB.get(method.getName()).add(method);
            }
        }

        var equalsInA = new ArrayList<Method>();
        var equalsInB = new ArrayList<Method>();
        for (var method: a.getDeclaredMethods()) {
            for (var methodB: methodsB.get(method.getName())) {
                if (isEqual(method, methodB)) {
                    equalsInA.add(method);
                    equalsInB.add(methodB);
                }
            }
        }
        for (var method: equalsInA) {
            methodsA.get(method.getName()).remove(method);
        }
        for (var method: equalsInB) {
            methodsB.get(method.getName()).remove(method);
        }

        var stringBuilder = new StringBuilder();
        methodsA.forEach((name, methods) -> {
            methods.forEach((method) -> {
                stringBuilder.append(method.toGenericString()).append("\n");});
        });
        methodsB.forEach((name, methods) -> {
            methods.forEach((method) -> {
                stringBuilder.append(method.toGenericString()).append("\n");});
        });

        writer.write(stringBuilder.toString());
    }

    /**
     * Compares two fields.
     */
    private static boolean isEqual(Field field1, Field field2) {
        return (
                field1.getModifiers() == field2.getModifiers() &&
                field1.getName().equals(field2.getName()) &&
                isEqual(field1.getType(), field2.getType()) &&
                isEqual(field1.getGenericType(), field2.getGenericType())
                );
    }

    /**
     * Compares two methods.
     */
    private static boolean isEqual(Method method1, Method method2) {
        return (
                method1.getModifiers() == method2.getModifiers() &&
                isEqual(method1.getReturnType(), method2.getReturnType()) &&
                isEqual(method1.getGenericReturnType(), method2.getGenericReturnType()) &&
                method1.getName().equals(method2.getName()) &&
                isEqual(method1.getReturnType(), method2.getReturnType()) &&
                isEqual(method1.getGenericParameterTypes(), method2.getGenericParameterTypes())
                );
    }

    /**
     * Compares two types.
     */
    private static boolean isEqual(Type type1, Type type2) {
        if (!type1.getTypeName().equals(type2.getTypeName())) {
            return false;
        }

        if (type1.getClass().getSuperclass() == Object.class &&
            type2.getClass().getSuperclass() == Object.class) {
            return true;
        }

        return isEqual(type1.getClass().getSuperclass(), type2.getClass().getSuperclass()) &&
               isEqual(type1.getClass().getInterfaces(), type2.getClass().getInterfaces());
    }

    /**
     * Compares two sets of types.
     * @return true if k-th position of the first set equals the k-th position of the second one for each k
     */
    private static boolean isEqual(Type[] types1, Type[] types2) {
        if (types1.length != types2.length) {
            return false;
        }

        for (int i = 0; i < types1.length; i++) {
            if (!isEqual(types1[i], types2[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Prints structure of a class.
     * @param someClass class for printing
     * @param writer output writer
     * @param prefix prefix of each line of output
     */
    public static void printClass(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        printClassHeading(someClass, writer, prefix);
        writer.write(" {\n");

        for (var innerClass: someClass.getDeclaredClasses()) {
            printClass(innerClass, writer, prefix + "\t");
        }

        printAllFields(someClass, writer, prefix + "\t");
        printAllConstructors(someClass, writer, prefix + "\t");
        printAllMethods(someClass, writer, prefix + "\t");
        writer.write(prefix + "}\n");

        writer.flush();
    }

    /**
     * Prints heading of a class (modifiers, class name, parent class and implemented interfaces).
     * Also prints all generic types.
     * @param someClass class for printing
     * @param writer output writer
     */
    private static void printClassHeading(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        writer.write(prefix + Modifier.toString(someClass.getModifiers()) + " class ");
        writer.write(someClass.getCanonicalName().replace('$', '.'));

        printTypeParameters(someClass.getTypeParameters(), writer);

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
     * Prints all constructor of a class.
     * @param someClass class for printing.
     * @param writer output writer
     * @param prefix prefix of each line of output
     */
    private static void printAllConstructors(Class<?> someClass, OutputStreamWriter writer, String prefix) throws IOException {
        for (var constructor: someClass.getDeclaredConstructors()) {
            if (constructor.isSynthetic()) {
                continue;
            }

            writer.write(prefix + constructor.getName().replace('$', '.'));
            printArguments(constructor.getGenericParameterTypes(), writer);
            writer.write(" {\n" + prefix + "}\n");
        }
    }

    /**
     * Prints all methods of class (private, public etc) with generic types.
     * Also prints all throwing exception.
     * @param someClass class for printing
     * @param writer output writer
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
            writer.write(method.getName());
            printArguments(method.getGenericParameterTypes(), writer);

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

    /**
     * Prints generic type parameters.
     * @param parameters generic type parameters for printing.
     * @param writer output writer
     */
    private static void printTypeParameters(TypeVariable[] parameters, OutputStreamWriter writer) throws IOException {
        if (parameters.length == 0) {
            return;
        }

        writer.write("<");
        for (int i = 0; i < parameters.length - 1; i++) {
            writer.write(parameters[i].getName());

            var bounds = parameters[i].getBounds();
            if (bounds.length > 0) {
                writer.write(" extends ");

                for (int j = 0; j < parameters.length - 1; j++) {
                    writer.write(bounds[j].getTypeName() + "& ");
                }
                writer.write(bounds[bounds.length - 1].getTypeName());
            }

            writer.write(", ");
        }

        writer.write(parameters[parameters.length - 1].getName());
        var bounds = parameters[parameters.length - 1].getBounds();
        if (bounds.length > 0) {
            writer.write(" extends ");

            for (int j = 0; j < parameters.length - 1; j++) {
                writer.write(bounds[j].getTypeName() + "& ");
            }
            writer.write(bounds[bounds.length - 1].getTypeName() + ">");
        }
    }

    /**
     * Prints arguments of a function.
     * @param types array of arguments
     * @param writer output writer
     */
    private static void printArguments(Type[] types, OutputStreamWriter writer) throws IOException {
        if (types.length == 0) {
            writer.write("()");
            return;
        }

        writer.write("(");
        for (int i = 0; i < types.length - 1; i++) {
            writer.write(types[i].getTypeName().replace('$', '.'));
            printTypeParameters(types[i].getClass().getTypeParameters(), writer);
            writer.write(", ");
        }
        writer.write(types[types.length - 1].getTypeName().replace('$', '.'));
        printTypeParameters(types[types.length - 1].getClass().getTypeParameters(), writer);
        writer.write(")");
    }
}