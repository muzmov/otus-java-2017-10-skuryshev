package net.kuryshev.front;

import net.kuryshev.ms.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressContext {
    private String msHost;
    private int msPort;
    private int myPort;

    public String getMsHost() {
        return msHost;
    }

    public void setMsHost(String msHost) {
        this.msHost = msHost;
    }

    public int getMsPort() {
        return msPort;
    }

    public void setMsPort(int msPort) {
        this.msPort = msPort;
    }

    public void setMyPort(int myPort) {
        this.myPort = myPort;
    }

    public int getMyPort() {
        return myPort;
    }
}
