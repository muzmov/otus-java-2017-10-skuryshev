package net.kuryshev.test;

import net.kuryshev.annotation.After;
import net.kuryshev.annotation.Before;
import net.kuryshev.annotation.Test;

public class CorrectTestClass {

    @Before
    public void setUp() {
        System.out.println("before");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }

    @After
    public void tearDown() {
        System.out.println("after");
    }
}
