package net.kuryshev;

import java.util.Arrays;

public class Sorter {
    private int numThreads;
    private MyThreadPoolExecutor executor;

    public Sorter(int numThreads) {
        if (!isPowerOfTwo(numThreads)) throw new IllegalArgumentException("numThreades should be a power of 2!");
        this.numThreads = numThreads;
        executor = new MyThreadPoolExecutor(numThreads);
    }

    private boolean isPowerOfTwo(int n) {
        return n >= 0 && (n & -n) == n;
    }

    public void sort(int[] array) {
        for (int i = 0; i < numThreads; i++) {
            int startIndex = array.length * i / numThreads;
            int endIndex = array.length * (i + 1) / numThreads;
            executor.addTask(() -> Arrays.sort(array, startIndex, endIndex));
        }
        executor.waitForAllTasksToBeProcessed();
        parallelMerge(array);
    }

    private void parallelMerge(int[] array) {
        int numParts = numThreads;
        while (numParts > 1) {
            numParts /= 2;
            for (int i = 0; i < numParts; i++) {
                int startIndex = array.length * i / numParts;
                int endIndex = array.length * (i + 1) / numParts;
                executor.addTask(() -> merge(array, startIndex, endIndex));
            }
            executor.waitForAllTasksToBeProcessed();
        }
    }

    private void merge(int[] array, int startIndex, int endIndex) {
        int a[] = merge0(array, startIndex, endIndex);
        System.arraycopy(a, 0, array, startIndex, a.length);
    }

    private int[] merge0(int[] a, int startIndex, int endIndex) {
        int halfIndex = (startIndex + endIndex) / 2;
        int[] answer = new int[endIndex - startIndex];
        int i = startIndex, j = halfIndex, k = 0;

        while (i < halfIndex && j < endIndex)
            answer[k++] = a[i] < a[j] ? a[i++] : a[j++];

        while (i < halfIndex) answer[k++] = a[i++];
        while (j < endIndex) answer[k++] = a[j++];

        return answer;
    }
}
