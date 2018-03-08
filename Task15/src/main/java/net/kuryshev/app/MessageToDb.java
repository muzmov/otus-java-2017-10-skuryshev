package net.kuryshev.app;

import net.kuryshev.messageSystem.Address;
import net.kuryshev.messageSystem.Addressee;
import net.kuryshev.messageSystem.Message;

public class MessageToDb extends Message {

    public MessageToDb(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) exec((DBService) addressee);
    }

    private void exec(DBService dbService) {
        String stats = dbService.getCacheStatistics();
        dbService.getMS().sendMessage(new MessageToFront(getTo(), getFrom(), stats));
    }
}
