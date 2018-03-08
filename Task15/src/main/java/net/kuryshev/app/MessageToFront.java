package net.kuryshev.app;

import net.kuryshev.front.ws.CacheEndpoint;
import net.kuryshev.front.ws.WsMessage;
import net.kuryshev.messageSystem.Address;
import net.kuryshev.messageSystem.Addressee;
import net.kuryshev.messageSystem.Message;

public class MessageToFront extends Message {

    private String content;

    public MessageToFront(Address from, Address to, String content) {
        super(from, to);
        this.content = content;
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof CacheEndpoint) exec((CacheEndpoint) addressee);
    }

    public void exec(CacheEndpoint ce) {
        WsMessage message = new WsMessage();
        message.setContent(content);
        System.out.println("Sending response:" + ce.getAddress().getId());
        ce.sendMessage(message);
    }
}
