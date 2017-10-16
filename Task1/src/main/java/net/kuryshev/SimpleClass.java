package net.kuryshev;

import com.google.common.collect.Lists;

import java.util.List;

public class SimpleClass {
    public static void main(String[] args) {
        List<Integer> list =  Lists.newArrayList(1, 2, 3, 4, 5);
        list.stream().forEachOrdered(System.out::print);
        System.out.println();
        list = Lists.reverse(list);
        list.stream().forEachOrdered(System.out::print);
    }
}
