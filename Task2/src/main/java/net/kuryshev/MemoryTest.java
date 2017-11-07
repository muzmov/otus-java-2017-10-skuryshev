package net.kuryshev;

import java.io.*;
import java.util.function.Supplier;

public class MemoryTest {
    private static final int ARRAY_SIZE = 100_000;

    public Object[] testSerializable(Serializable testedObject) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        Object[] array = new Object[ARRAY_SIZE];
        System.gc();

        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            ous.writeObject(testedObject);
            ous.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            array[i] = ois.readObject();
        }

        System.gc();

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println((finalMemory - initialMemory) / ARRAY_SIZE);
        return array;
    }

    public <T> Object[] testSupplier(Supplier<T> supplier) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        Object[] array = new Object[ARRAY_SIZE];
        System.gc();

        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = supplier.get();
        }

        System.gc();

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println((finalMemory - initialMemory) / ARRAY_SIZE);
        return array;
    }
}
