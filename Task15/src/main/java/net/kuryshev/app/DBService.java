package net.kuryshev.app;

import net.kuryshev.backend.entity.DataSet;
import net.kuryshev.messageSystem.Addressee;

public interface DBService extends Addressee {

    void prepareTables();

    <T extends DataSet> T load(long id, Class<T> clazz);

    <T extends DataSet> void save(T entity);

    void close();

    void printCacheStatistics();

    String getCacheStatistics();
}
