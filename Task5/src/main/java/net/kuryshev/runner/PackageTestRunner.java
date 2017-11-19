package net.kuryshev.runner;

import net.kuryshev.exception.IncorrectTestClassException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageTestRunner {
    private String packageName;
    private List<Class> testClasses = new ArrayList<>();

    public PackageTestRunner(String packageName) {
        this.packageName = packageName;
    }

    public void run() {
        fillTestClassesList();
        runTests();
    }

    private void fillTestClassesList() {
        File[] files = getFiles(getRoot());

        if (files != null)
            for (File file : files)
                if (isClass(file)) addClass(file);
    }

    private URL getRoot() {
        return Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
    }

    private File[] getFiles(URL root) {
        File[] files = null;
        if (root != null) files = new File(root.getFile()).listFiles((dir, name) -> name.endsWith(".class"));
        return files;
    }

    private boolean isClass(File file) {
        return file.getName().matches(".*\\.class$");
    }

    private void addClass(File file) {
        try {
            String className = file.getName().replaceAll("\\.class$", "");
            Class<?> cls = Class.forName(packageName + "." + className);
            testClasses.add(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void runTests() {
        for (Class testClass : testClasses) {
            ClassTestRunner runner = new ClassTestRunner(testClass);
            try {
                runner.run();
            } catch (IncorrectTestClassException e) {
                System.out.println("Testing class " + testClass.getName() + " produced IncorrectTestClassException");
            }
        }
    }
}
