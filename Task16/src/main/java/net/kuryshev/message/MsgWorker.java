package net.kuryshev.message;

import net.kuryshev.app.Mode;
import net.kuryshev.message.Msg;
import net.kuryshev.ms.Address;

import java.io.IOException;

public interface MsgWorker {
    void send(Msg msg);

    Msg pool();

    Msg take() throws InterruptedException;

    void close() throws IOException;

    Mode getMode();

    void setMode(Mode mode);

    Address getAddress();

    void setAddress(Address address);
}