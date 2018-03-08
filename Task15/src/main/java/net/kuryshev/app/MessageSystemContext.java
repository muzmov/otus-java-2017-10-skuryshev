package net.kuryshev.app;

import net.kuryshev.front.ws.CacheEndpoint;
import net.kuryshev.messageSystem.Address;
import net.kuryshev.messageSystem.MessageSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MessageSystemContext {
    @Autowired
    private MessageSystem ms;

    private final List<Address> dbAddresses = new ArrayList<>();
    private final List<Address> frontAddresses = new ArrayList<>();

    public MessageSystem getMs() {
        return ms;
    }

    public void setMs(MessageSystem ms) {
        this.ms = ms;
    }

    private void addFrontAddress(Address address) {
        frontAddresses.add(address);
    }

    private void removeFrontAddress(Address address) {
        frontAddresses.remove(address);
    }

    private void addDbAddress(Address address) {
        dbAddresses.add(address);
    }

    private void removeDbAddress(Address address) {
        dbAddresses.remove(address);
    }

    public Address getDbAddress() {
        Collections.shuffle(dbAddresses);
        return dbAddresses.stream().findFirst().orElse(null);
    }

    public Address getFrontAddress() {
        Collections.shuffle(frontAddresses);
        return frontAddresses.stream().findFirst().orElse(null);
    }

    public void addDbAddressee(DBService dbService) {
        ms.addAddressee(dbService);
        addDbAddress(dbService.getAddress());
    }

    public void addFrontAddressee(CacheEndpoint cacheEndpoint) {
        ms.addAddressee(cacheEndpoint);
        addFrontAddress(cacheEndpoint.getAddress());
    }

    public void removeDbAddressee(DBService dbService) {
        ms.removeAddressee(dbService);
        removeDbAddress(dbService.getAddress());
        System.out.println("Address " + dbService.getAddress().getId() + " was successfully removed from context!");
    }

    public void removeFrontAddressee(CacheEndpoint cacheEndpoint) {
        ms.removeAddressee(cacheEndpoint);
        removeFrontAddress(cacheEndpoint.getAddress());
        System.out.println("Address " + cacheEndpoint.getAddress().getId() + " was successfully removed from context!");
    }

}
