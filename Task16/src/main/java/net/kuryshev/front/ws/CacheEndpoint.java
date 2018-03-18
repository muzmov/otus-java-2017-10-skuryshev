package net.kuryshev.front.ws;

import net.kuryshev.front.AddressContext;
import net.kuryshev.front.socket.ClientSocketMsgWorker;
import net.kuryshev.message.Msg;
import net.kuryshev.message.MsgRegisterFront;
import net.kuryshev.message.MsgToDb;
import net.kuryshev.message.MsgToFront;
import net.kuryshev.ms.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sergey.Kuryshev on 09.02.2018
 */
@ServerEndpoint(value = "/cache", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class CacheEndpoint {

    private Session session;
    private ClientSocketMsgWorker client;
    private ExecutorService executorService;

    @Autowired
    private AddressContext addressContext;
    private Address myAddress;

    @SuppressWarnings("InfiniteLoopStatement")
    private void startSocketWorker() throws Exception {
        client = new ClientSocketMsgWorker(addressContext.getMsHost(), addressContext.getMsPort());
        client.init();
        client.send(new MsgRegisterFront(myAddress));

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    MsgToFront msg = (MsgToFront) client.take();
                    System.out.println("Message received: " + msg.toString());
                    WsMessage message = new WsMessage();
                    message.setContent(msg.getText());
                    sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMessage(WsMessage message) throws Exception {
        session.getBasicRemote().sendObject(message);
    }

    @OnOpen
    public void onOpen(Session session) throws Exception {
        this.session = session;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        myAddress = new Address(addressContext.getMyPort(), "front");
        startSocketWorker();
    }

    @OnMessage
    public void onMessage(WsMessage wsMessage) throws Exception {
        if (wsMessage.getType() == WsMessage.Type.GET_CACHE_STATS) {
            Msg msg = new MsgToDb(myAddress);
            client.send(msg);
            System.out.println("Message sent: " + msg.toString());
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        client.close();
        executorService.shutdown();
    }

}
