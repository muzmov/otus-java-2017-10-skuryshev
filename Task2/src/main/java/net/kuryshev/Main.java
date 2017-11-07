package net.kuryshev;

import java.io.Serializable;
import java.util.*;

public class Main implements Serializable {
    private final static int TEST_SIZE = 10;
    
    public static void main(String[] args) throws Exception {
        MemoryTest mt = new MemoryTest();
        
        Integer[] array = new Integer[TEST_SIZE];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, array);
        LinkedList<Integer> linkedList = new LinkedList<>();
        Collections.addAll(linkedList, array);
        HashSet<Integer> hashSet = new HashSet<>();
        Collections.addAll(hashSet, array);
        TreeSet<Integer> treeSet = new TreeSet<>();
        Collections.addAll(treeSet, array);

        System.out.println("Testing serializable objects");
        System.out.println("Empty String: ");
        mt.testSerializable("");
        System.out.println("One letter String: ");
        mt.testSerializable("a");
        System.out.println("New Main(): ");
        mt.testSerializable(new Main());
        System.out.println("Empty Array: ");
        mt.testSerializable(new Object[]{});
        System.out.println("Empty ArrayList: ");
        mt.testSerializable(new ArrayList<>());
        System.out.println(TEST_SIZE + " Integers Array: ");
        mt.testSerializable(array);
        System.out.println(TEST_SIZE + " Integers ArrayList: ");
        mt.testSerializable(arrayList);
        System.out.println(TEST_SIZE + " Integers LinkedList: ");
        mt.testSerializable(linkedList);
        System.out.println(TEST_SIZE + " Integers HashSet: ");
        mt.testSerializable(hashSet);
        System.out.println(TEST_SIZE + " Integers TreeSet: ");
        mt.testSerializable(treeSet);


        System.out.println("\nTesting objects created with Supplier");
        System.out.println("New String: ");
        mt.testSupplier(String::new);
        System.out.println("One letter String: ");
        mt.testSupplier(() -> new String("a"));
        System.out.println("New Main(): ");
        mt.testSupplier(Main::new);
        System.out.println("Empty Array: ");
        mt.testSupplier(() -> new Object[]{});
        System.out.println("Empty ArrayList: ");
        mt.testSupplier(ArrayList::new);
        System.out.println(TEST_SIZE + " Integers ArrayList: ");
        mt.testSupplier(() -> {
            ArrayList<Integer> result = new ArrayList<>();
            Collections.addAll(result, array);
            return result;
        });
        System.out.println(TEST_SIZE + " Integers LinkedList: ");
        mt.testSupplier(() -> {
            LinkedList<Integer> result = new LinkedList<>();
            Collections.addAll(result, array);
            return result;
        });
        System.out.println(TEST_SIZE + " Integers HashSet: ");
        mt.testSupplier(() -> {
            HashSet<Integer> result = new HashSet<>();
            Collections.addAll(result, array);
            return result;
        });
        System.out.println(TEST_SIZE + " Integers TreeSet: ");
        mt.testSupplier(() -> {
            TreeSet<Integer> result = new TreeSet<>();
            Collections.addAll(result, array);
            return result;
        });
    }
}
