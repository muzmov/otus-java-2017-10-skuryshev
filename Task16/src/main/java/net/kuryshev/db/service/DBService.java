package net.kuryshev.db.service;

import net.kuryshev.db.dataset.DataSet;

public interface DBService {

    void prepareTables();

    <T extends DataSet> T load(long id, Class<T> clazz);

    <T extends DataSet> void save(T entity);

    void close();

    void printCacheStatistics();

    String getCacheStatistics();
}
