package net.kuryshev;

import java.io.Serializable;
import java.lang.reflect.Array;

public class Main implements Serializable{
    public static void main(String[] args) throws Exception {
        MemoryTest mt = new MemoryTest();
        System.out.println("Testing serializable objects");
        System.out.println("Empty String: ");
        mt.testSerializable("");
        System.out.println("New String: ");
        mt.testSerializable(new String());
        System.out.println("One letter String: ");
        mt.testSerializable("a");
        System.out.println("New Main(): ");
        mt.testSerializable(new Main());
        System.out.println("Empty Array: ");
        mt.testSerializable(new Object[]{});

        System.out.println("\nTesting objects created with Supplier");
        System.out.println("New String: ");
        mt.testSupplier(String::new);
        System.out.println("New Main(): ");
        mt.testSupplier(Main::new);
        System.out.println("Empty Array: ");
        mt.testSupplier(() -> new Object[]{});
    }
}
