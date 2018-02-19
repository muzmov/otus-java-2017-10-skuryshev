package net.kuryshev;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPoolExecutor {
    private int numThreads;
    private volatile boolean isRunning = true;
    private final Queue<Runnable> workingQueue = new ConcurrentLinkedQueue<>();
    private final List<Thread> threads = new ArrayList<>();
    private AtomicInteger addedTasksCount = new AtomicInteger(0);
    private AtomicInteger processedTasksCount = new AtomicInteger(0);

    public MyThreadPoolExecutor(int numThreads) {
        this.numThreads = numThreads;
        startThreads();
    }

    private void startThreads() {
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(this::worker);
            threads.add(thread);
            thread.start();
        }
    }

    private void worker() {
        while (isRunning) {
            Runnable task = workingQueue.poll();
            if (task != null) {
                task.run();
                processedTasksCount.addAndGet(1);
            }
        }
    }

    public void stop() {
        isRunning = false;
        join();
    }

    private void join() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTask(Runnable task) {
        if (isRunning) {
            addedTasksCount.addAndGet(1);
            workingQueue.offer(task);
        }
    }

    public void waitForAllTasksToBeProcessed() {
        while (processedTasksCount.get() != addedTasksCount.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
