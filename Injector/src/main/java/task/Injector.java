package task;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Injector {

    static class Node {

        private String name;

        private ArrayList<Node> parents;
        private ArrayList<Node> children;

        Node(String name, ArrayList<Node> parents) {
            this.name = name;
            this.parents = parents;
            children = new ArrayList<>();
        }
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Node root = buildTree(rootClassName, implementationClassNames, new HashMap<String, Node>());
        if (!checkTreeCorrectness(root, new ArrayList<>())) {
            throw new InjectionCycleException();
        }

        return initializeByTree(root);
    }

    private static Object initializeByTree(Node root) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<Object> parameters = new ArrayList<>();

        for (Node current: root.children) {
            parameters.add(initializeByTree(current));
        }

        Class<?> rootClass = Class.forName(root.name);
        Constructor<?> rootConstructor = rootClass.getConstructor();

        return rootConstructor.newInstance(parameters.toArray());
    }

    private static Node buildTree(String rootClassName, List<String> implementationClassNames, HashMap<String, Node> treeNodes) throws Exception {
        Node root = new  Node(rootClassName, new ArrayList<>());
        Class<?> rootClass = Class.forName(rootClassName);
        Constructor<?> rootConstructor = rootClass.getConstructor();
        treeNodes.put(rootClassName, root);

        for (Class current: rootConstructor.getParameterTypes()) {
            int numberOfClasses = 0;
            for (String name: implementationClassNames) {
                if (name.equals(current.getCanonicalName())) {
                    numberOfClasses++;
                }
            }

            if (numberOfClasses == 0) {
                throw new ImplementationNotFoundException();
            } else if (numberOfClasses > 1) {
                throw new AmbiguousImplementationException();
            }

            Node child = treeNodes.get(current.getCanonicalName());
            if (child == null) {
                child = buildTree(current.getCanonicalName(), implementationClassNames, treeNodes);
            }
            child.parents.add(root);
            root.children.add(child);
        }

        return root;
    }

    private static boolean checkTreeCorrectness(Node root, ArrayList<Node> way) {
        if (way.contains(root)) {
            return false;
        }

        way.add(root);
        for (Node child: root.children) {
            if (!checkTreeCorrectness(child, way)) {
                return false;
            }
        }
        way.remove(root);

        return true;
    }
}