package net.kuryshev;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//-Xmx512m -Xms512m -XX:+UseSerialGC -Xloggc:memoryLeak-Serial.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps
//-Xmx512m -Xms512m -XX:+UseParallelGC -XX:+UseParallelOldGC -Xloggc:memoryLeak-Parallel.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps
//-Xmx512m -Xms512m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC  -Xloggc:memoryLeak-ParNew.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps
//-Xmx512m -Xms512m -XX:+UseG1GC -Xloggc:memoryLeak-G1.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps
public class MemoryLeak {

    public static final int ITERATIONS = 1500000;

    public static void main(String[] args) throws Exception {
        List<Object> list = new ArrayList<>();
        System.out.println("started");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            add(list, 1000);
            remove(list, 990);
            if (i % 100000 == 0) System.out.println(i);
        }
        System.out.println("Finished after " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    public static void add(@NotNull List<Object> list, int numElems) {
        for (int i = 0; i < numElems; i++) {
            list.add(new Object());
        }
    }

    public static void remove(@NotNull List<Object> list, int numElems) {
        if (list.size() < numElems) return;
        int listSize = list.size();
        for (int i = listSize - 1; i > listSize - numElems - 1; i--) {
            list.remove(i);
        }
    }
}
