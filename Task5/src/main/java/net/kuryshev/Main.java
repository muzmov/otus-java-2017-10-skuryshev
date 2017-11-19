package net.kuryshev;

import net.kuryshev.exception.IncorrectTestClassException;
import net.kuryshev.runner.ClassTestRunner;
import net.kuryshev.runner.PackageTestRunner;
import net.kuryshev.test.CorrectTestClass;
import net.kuryshev.test.IncorrectTestClass;

public class Main {

    public static void main(String[] args) {
        System.out.println("ClassTestRunner:");
        ClassTestRunner classTestRunner = new ClassTestRunner(CorrectTestClass.class);
        classTestRunner.run();
        classTestRunner = new ClassTestRunner(IncorrectTestClass.class);
        try {
            classTestRunner.run();
        } catch (IncorrectTestClassException e) {
            System.out.println("Testing IncorrectTestClass with multiple @Before produced IncorrectTestClassException");
        }

        System.out.println("PackageTestRunner (doesn't work from jar):");
        PackageTestRunner packageTestRunner = new PackageTestRunner("net.kuryshev.test");
        packageTestRunner.run();
    }
}
