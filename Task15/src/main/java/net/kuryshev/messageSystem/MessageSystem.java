package net.kuryshev.messageSystem;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());
    private static final int DEFAULT_STEP_TIME = 10;

    private final Map<Address, Thread> workers;
    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap;

    public MessageSystem() {
        workers = new HashMap<>();
        messagesMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        messagesMap.put(addressee.getAddress(), new ConcurrentLinkedQueue<>());
        startWorker(addressee);
        System.out.println("Addressee with address " + addressee.getAddress().getId() + " added successfully!");
    }

    public void removeAddressee(Addressee addressee) {
        stopWorker(addressee);
        messagesMap.remove(addressee.getAddress());
        System.out.println("Addressee with address " + addressee.getAddress().getId() + " removed successfully!");
    }

    private void stopWorker(Addressee addressee) {
        Thread worker = workers.get(addressee.getAddress());
        if (worker == null) return;
        worker.interrupt();
    }

    private void startWorker(Addressee addressee) {
        String name = "MS-worker-" + addressee.getAddress().getId();
        Thread thread = new Thread(() -> {
            while (true) {

                ConcurrentLinkedQueue<Message> queue = messagesMap.get(addressee.getAddress());
                while (!queue.isEmpty()) {
                    Message message = queue.poll();
                    message.exec(addressee);
                }
                try {
                    Thread.sleep(MessageSystem.DEFAULT_STEP_TIME);
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                }
                if (Thread.currentThread().isInterrupted()) {
                    logger.log(Level.INFO, "Finishing: " + name);
                    return;
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.put(addressee.getAddress(), thread);
    }

    public void sendMessage(Message message) {
        Address to = message.getTo();
        ConcurrentLinkedQueue<Message> queue = messagesMap.get(to);
        queue.add(message);
    }

}

