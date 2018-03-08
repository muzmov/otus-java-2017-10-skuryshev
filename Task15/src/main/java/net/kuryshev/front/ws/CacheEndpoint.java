package net.kuryshev.front.ws;

import net.kuryshev.app.MessageSystemContext;
import net.kuryshev.app.MessageToDb;
import net.kuryshev.messageSystem.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by Sergey.Kuryshev on 09.02.2018
 */
@ServerEndpoint(value="/cache", decoders = MessageDecoder.class, encoders = MessageEncoder.class )
public class CacheEndpoint implements Addressee {

    @Autowired
    private MessageSystemContext msContext;
    private Session session;
    private Address address = new Address("front");

    public void sendMessage(WsMessage message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        msContext.addFrontAddressee(this);
        System.out.println("Socket opened:" + address.getId());
    }

    @OnMessage
    public void onMessage(WsMessage wsMessage) {
        if (wsMessage.getType() == WsMessage.Type.GET_CACHE_STATS) {
            Message message = new MessageToDb(getAddress(), msContext.getDbAddress());
            msContext.getMs().sendMessage(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        msContext.removeFrontAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return msContext.getMs();
    }
}
