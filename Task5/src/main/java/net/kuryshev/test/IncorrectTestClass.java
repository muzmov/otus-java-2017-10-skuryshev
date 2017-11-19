package net.kuryshev.test;

import net.kuryshev.annotation.After;
import net.kuryshev.annotation.Before;
import net.kuryshev.annotation.Test;

public class IncorrectTestClass {

    @Before
    public void setUp1() {
        System.out.println("before1");
    }

    @Before
    public void setUp2() {
        System.out.println("before2");
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
