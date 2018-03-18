package net.kuryshev.ms;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final String id;
    private final int port;

    public Address(int port){
        this.port = port;
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    public Address(int port, String id) {
        this.port = port;
        this.id = id + String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return port == address.port && Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, port);
    }

    @Override
    public String toString() {
        return "Address{" + "id='" + id + '\'' + ", port=" + port + '}';
    }
}