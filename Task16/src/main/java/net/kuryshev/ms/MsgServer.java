package net.kuryshev.ms;

import net.kuryshev.app.Mode;
import net.kuryshev.message.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class MsgServer {
    private static final Logger logger = Logger.getLogger(MsgServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 8080;
    private static final int MIRROR_DELAY_MS = 100;

    private final ExecutorService executor;
    private final List<MsgWorker> clients;

    public MsgServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        clients = new CopyOnWriteArrayList<>();
    }

    public void start() throws Exception {
        executor.submit(this::mirror);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket socket = serverSocket.accept(); //blocks
                SocketMsgWorker client = new SocketMsgWorker(socket);
                client.init();
                clients.add(client);
                Msg msg = client.take();
                if (msg instanceof MsgRegisterDb) {
                    System.out.println("-----------------------DB registered: " + msg);
                    client.setMode(Mode.DB);
                }
                if (msg instanceof MsgRegisterFront) {
                    System.out.println("-----------------------Front registered: " + msg);
                    client.setMode(Mode.FRONT);
                    client.setAddress(msg.getFrontAddress());
                }
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void mirror() {
        while (true) {
            for (MsgWorker client : clients) {
                Msg msg = client.pool();
                while (msg != null) {
                    if (msg instanceof MsgToFront) {
                        System.out.println("-------------------------Sending msg to front: " + msg.toString());
                        MsgToFront msgToFront = (MsgToFront) msg;
                        clients.stream()
                                .filter(c -> c.getMode() == Mode.FRONT && Objects.equals(c.getAddress(), msgToFront.getFrontAddress()))
                                .forEach(c -> c.send(msgToFront));
                    }
                    if (msg instanceof MsgToDb) {
                        System.out.println("-----------------------Sending msg to db: " + msg.toString());
                        MsgToDb msgToDb = (MsgToDb) msg;
                        clients.stream().filter(c -> c.getMode() == Mode.DB).findAny().ifPresent(c -> c.send(msgToDb));
                    }
                    msg = client.pool();
                }
            }
            try {
                Thread.sleep(MIRROR_DELAY_MS);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    public boolean getRunning() {
        return true;
    }

    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Bye.");
        }
    }
}
