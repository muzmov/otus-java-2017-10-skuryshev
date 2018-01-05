package net.kuryshev.em;

public interface EmFactory {
    MyEntityManager createEntityManager(String connectionUrl);
}
