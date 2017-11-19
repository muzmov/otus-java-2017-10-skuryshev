package net.kuryshev.runner;

import net.kuryshev.annotation.After;
import net.kuryshev.annotation.Before;
import net.kuryshev.annotation.Test;
import net.kuryshev.exception.IncorrectTestClassException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassTestRunner {
    private Method beforeMethod;
    private Method afterMethod;
    private List<Method> testMethods = new ArrayList<>();
    private Class testClass;

    public ClassTestRunner(Class testClass) {
        this.testClass = testClass;
    }

    public void run() {
        prepareTests();
        try {
            runTests();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IncorrectTestClassException();
        }
    }

    private void prepareTests() {
        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            checkBefore(method);
            checkAfter(method);
            checkTest(method);
        }
    }

    private void checkTest(Method method) {
        if (method.getDeclaredAnnotation(Test.class) != null) {
            if (isCorrectTestMethod(method)) testMethods.add(method);
            else throw new IncorrectTestClassException();
        }
    }

    private boolean isCorrectTestMethod(Method method) {
        return method.getDeclaredAnnotation(After.class) == null && method.getDeclaredAnnotation(Before.class) == null;
    }

    private void checkAfter(Method method) {
        if (method.getDeclaredAnnotation(After.class) != null) {
            if (isCorrectAfterMethod(method)) afterMethod = method;
            else throw new IncorrectTestClassException();
        }
    }

    private boolean isCorrectAfterMethod(Method method) {
        return afterMethod == null && method.getDeclaredAnnotation(Before.class) == null && method.getDeclaredAnnotation(Test.class) == null;
    }

    private void checkBefore(Method method) {
        if (method.getDeclaredAnnotation(Before.class) != null) {
            if (isCorrectBeforeMethod(method)) beforeMethod = method;
            else throw new IncorrectTestClassException();
        }
    }

    private boolean isCorrectBeforeMethod(Method method) {
        return beforeMethod == null && method.getDeclaredAnnotation(After.class) == null && method.getDeclaredAnnotation(Test.class) == null;
    }

    private void runTests() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        for (Method testMethod : testMethods) {
            Object testClassObject = testClass.newInstance();
            if (beforeMethod != null) beforeMethod.invoke(testClassObject);
            testMethod.invoke(testClassObject);
            if (afterMethod != null) afterMethod.invoke(testClassObject);
        }
    }
}
